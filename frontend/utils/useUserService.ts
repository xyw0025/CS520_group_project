'use client';

import { create } from 'zustand';
import { useRouter, useSearchParams } from 'next/navigation';
import { useAlertService, useFetch } from 'utils';

export { useUserService };

// user state store
const initialState = {
  matchedUsers: undefined,
  undiscoveredUsers: undefined,
  currentUser: undefined,
};
const userStore = create<IUserStore>(() => initialState);

function useUserService(): IUserService {
  const alertService = useAlertService();
  const fetch = useFetch();
  const router = useRouter();
  const searchParams = useSearchParams();
  const { matchedUsers, undiscoveredUsers, currentUser } = userStore();
  const API_URL = process.env.NEXT_PUBLIC_API_URL;

  return {
    matchedUsers,
    undiscoveredUsers,
    currentUser,
    setUser: async (user) => {
      try {
        userStore.setState({ ...initialState, currentUser: user });
      } catch (error: any) {
        alertService.error(error);
      }
    },
    register: async (user) => {
      try {
        await fetch.post(`${API_URL}/api/v1/users/register`, user);
        alertService.success('Registration successful', true);
        router.push('/login');
      } catch (error: any) {
        alertService.error(error);
      }
    },
    login: async (email, password) => {
      alertService.clear();
      try {
        const currentUser = await fetch.post(`${API_URL}/api/v1/users/login}`, {
          email,
          password,
        });
        localStorage.setItem('currentUser', JSON.stringify(currentUser));
        userStore.setState({ ...initialState, currentUser });

        // get return url from query parameters or default to '/'
        // const returnUrl = searchParams.get('returnUrl') || '/chat';
        router.push('/profile');
      } catch (error: any) {
        alertService.error(error);
      }
    },
    logout: async () => {
      await fetch.post(`${API_URL}/api/v1/users/logout`);
      userStore.setState({ ...initialState });
      localStorage.removeItem('currentUser');
      router.push('/');
    },
    getCurrent: async () => {
      if (!currentUser) {
        const fetchedUser = await fetch.get(`${API_URL}/api/v1/users/current`);
        userStore.setState({ currentUser: fetchedUser });
        return fetchedUser;
      }
      return currentUser;
    },
    getMatchings: async (id) => {
      try {
        return await fetch.get(`${API_URL}/api/v1/users/matchings?id=${id}`);
      } catch (error: any) {
        alertService.error(error);
        return null;
      }
    },
    create: async (user) => {
      await fetch.post('/api/v1/users/register', user);
    },
    update: async (id, params) => {
      try {
        return await fetch.put(`${API_URL}/api/v1/profile/${id}`, params);
      } catch (error: any) {
        alertService.error(error);
      }
    },
    upload: async (id, name, photoFile) => {
      try {
        return await fetch.post(
          `${API_URL}/api/v1/users/${id}/upload`,
          {
            name,
            file: photoFile,
          },
          true
        );
      } catch (error: any) {
        console.log('error');
        alertService.error(error);
      }
    },
    // delete: async (id) => {
    //   // set isDeleting prop to true on user
    //   userStore.setState({
    //     users: users!.map((x) => {
    //       if (x.id === id) {
    //         x.isDeleted = true;
    //       }
    //       return x;
    //     }),
    //   });

    //   // delete user
    //   const response = await fetch.delete(`/api/v1/users/${id}`);

    //   // remove deleted user from state
    //   userStore.setState({ users: users!.filter((x) => x.id !== id) });

    //   // logout if the user deleted their own record
    //   if (response.deletedSelf) {
    //     router.push('/account/login');
    //   }
    // },
  };
}

// Interfaces
interface IUser {
  id: string;
  email: string;
  name: string;
  isDeleted: boolean;
  profile?: Profile;
}

interface Profile {
  displayName?: string;
  gender?: string;
  birthday?: string;
  major?: string;
  age?: number;
  bio?: string;
  imageUrls?: string[];
  preferences?: Preference[];
  isDeleted?: boolean;
}

interface Preference {
  name: string;
}

interface IUserStore {
  matchedUsers?: IUser[];
  undiscoveredUsers?: IUser[];
  currentUser?: IUser;
}

interface IUserService extends IUserStore {
  login: (username: string, password: string) => Promise<void>;
  logout: () => Promise<void>;
  register: (user: IUser) => Promise<void>;
  getCurrent: () => Promise<IUser>;
  getMatchings: (id: string) => Promise<IUser[]>;
  create: (user: IUser) => Promise<void>;
  update: (id: string, params: any) => Promise<IUser>;
  upload: (id: string, name: string, photoFile: File) => Promise<string>;
  // delete: (id: string) => Promise<void>;
  setUser: (user: IUser) => Promise<void>;
}
