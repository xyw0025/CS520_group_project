import { useRouter } from 'next/navigation';

export { useFetch };

function useFetch() {
  const router = useRouter();

  return {
    get: request('GET'),
    post: request('POST'),
    put: request('PUT'),
    delete: request('DELETE'),
  };

  function request(method: string) {
    return (url: string, body?: any, isFileUpload?: boolean) => {
      const requestOptions: any = {
        method,
        credentials: 'include',
      };

      if (isFileUpload && body) {
        // For file uploads, use FormData
        const formData = new FormData();
        Object.keys(body).forEach((key) => {
          formData.append(key, body[key]);
        });
        requestOptions.body = formData;
      } else if (body) {
        // For regular JSON data
        requestOptions.headers = { 'Content-Type': 'application/json' };
        requestOptions.body = JSON.stringify(body);
      }

      return fetch(url, requestOptions).then(handleResponse);
    };
  }

  // helper functions

  async function handleResponse(response: any) {
    const isJson = response.headers
      ?.get('content-type')
      ?.includes('application/json');
    const data = isJson ? await response.json() : await response.text();

    // check for error response
    if (!response.ok) {
      if (response.status === 401) {
        // api auto logs out on 401 Unauthorized, so redirect to login page
        router.push('/login');
      }
      // get error message from body or default to response status
      const error =
        (data && data.error) || (data && data.message) || response.statusText;
      return Promise.reject(error);
    }

    return data;
  }
}
