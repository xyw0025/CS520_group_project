'use client';

import { create } from 'zustand';
import { useRouter, useSearchParams } from 'next/navigation';
import { useAlertService, useFetch } from 'utils';

export { useUserService };

// user state store
const initialState = {
  users: undefined,
  user: undefined,
  currentUser: undefined,
};
const userStore = create<IUserStore>(() => initialState);

function useUserService(): IUserService {
  const alertService = useAlertService();
  const fetch = useFetch();
  const router = useRouter();
  const searchParams = useSearchParams();
  const { users, user, currentUser } = userStore();
  const API_URL = process.env.NEXT_PUBLIC_API_URL;

  return {
    users,
    user,
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
        const currentUser = await fetch.post(`${API_URL}/api/v1/users/login`, {
          email,
          password,
        });
        localStorage.setItem('currentUser', JSON.stringify(currentUser));
        userStore.setState({ ...initialState, currentUser });

        // get return url from query parameters or default to '/'
        const returnUrl = searchParams.get('returnUrl') || '/dashboard';
        router.push(returnUrl);
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
    getAll: async () => {
      userStore.setState({ users: await fetch.get('/api/v1/users/all') });
    },
    getById: async (id) => {
      userStore.setState({ user: undefined });
      try {
        userStore.setState({ user: await fetch.get(`/api/v1/users/${id}`) });
      } catch (error: any) {
        alertService.error(error);
      }
    },
    getCurrent: async () => {
      if (!currentUser) {
        const fetchedUser = await fetch.get(`${API_URL}/api/v1/users/current`);
        userStore.setState({ currentUser: fetchedUser });
        return fetchedUser;
      }
      return currentUser;
    },
    create: async (user) => {
      await fetch.post('/api/v1/users/register', user);
    },
    update: async (id, params) => {
      await fetch.put(`/api/v1/users/${id}`, params);

      // update current user if the user updated their own record
      if (id === currentUser?.id) {
        userStore.setState({ currentUser: { ...currentUser, ...params } });
      }
    },
    delete: async (id) => {
      // set isDeleting prop to true on user
      userStore.setState({
        users: users!.map((x) => {
          if (x.id === id) {
            x.isDeleted = true;
          }
          return x;
        }),
      });

      // delete user
      const response = await fetch.delete(`/api/v1/users/${id}`);

      // remove deleted user from state
      userStore.setState({ users: users!.filter((x) => x.id !== id) });

      // logout if the user deleted their own record
      if (response.deletedSelf) {
        router.push('/account/login');
      }
    },
  };
}

// interfaces

interface IUser {
  id: string;
  email: string;
  name: string;
  isDeleted: boolean;
}

interface IUserStore {
  users?: IUser[];
  user?: IUser;
  currentUser?: IUser;
}

interface IUserService extends IUserStore {
  login: (username: string, password: string) => Promise<void>;
  logout: () => Promise<void>;
  register: (user: IUser) => Promise<void>;
  getAll: () => Promise<void>;
  getById: (id: string) => Promise<void>;
  getCurrent: () => Promise<IUser>;
  create: (user: IUser) => Promise<void>;
  update: (id: string, params: Partial<IUser>) => Promise<void>;
  delete: (id: string) => Promise<void>;
  setUser: (user: IUser) => Promise<void>;
}
