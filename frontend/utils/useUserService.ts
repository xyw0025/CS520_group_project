'use client';

import { create } from 'zustand';
import { useRouter, useSearchParams } from 'next/navigation';
import { useFetch } from 'utils';
import { useToast } from '@/components/ui/use-toast';

export { useUserService };

// user state store
const initialState = {
  matchedUsers: undefined,
  undiscoveredUsers: undefined,
  currentUser: undefined,
};
const userStore = create<IUserStore>(() => initialState);

function useUserService(): IUserService {
  const fetch = useFetch();
  const router = useRouter();
  const searchParams = useSearchParams();
  const { toast } = useToast();
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
        toast({
          title: 'Uh oh! Something went wrong.',
          description: error,
          variant: 'destructive',
        });
      }
    },
    register: async (user) => {
      try {
        await fetch.post(`${API_URL}/api/v1/users/register`, user);
        toast({
          title: 'Register Successfully!',
        });
        router.push('/login');
      } catch (error: any) {
        console.log(error);
        toast({
          title: 'Registration Failed',
          description: error,
          variant: 'destructive',
        });
      }
    },
    login: async (email, password) => {
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
        toast({
          title: 'Login Successfully!',
        });
      } catch (error: any) {
        toast({
          title: 'Uh oh! Something went wrong.',
          description: error,
          variant: 'destructive',
        });
      }
    },
    logout: async () => {
      await fetch.post(`${API_URL}/api/v1/users/logout`);
      userStore.setState({ ...initialState });
      localStorage.removeItem('currentUser');
      router.push('/');
      toast({
        title: 'Logout Successfully!',
      });
    },
    getCurrent: async () => {
      if (!currentUser) {
        const fetchedUser = await fetch.get(`${API_URL}/api/v1/users/current`);
        userStore.setState({ currentUser: fetchedUser });
        return fetchedUser;
      }
      return currentUser;
    },
    update: async (id, params) => {
      try {
        const updated_user = await fetch.put(
          `${API_URL}/api/v1/profile/${id}`,
          params
        );
        toast({
          title: 'Update Profile Successfully!',
        });
        return updated_user;
      } catch (error: any) {
        toast({
          title: 'Uh oh! Something went wrong.',
          variant: 'destructive',
        });
      }
    },
    upload: async (id, name, photoFile) => {
      try {
        const imageUrl = await fetch.post(
          `${API_URL}/api/v1/users/${id}/upload`,
          {
            name,
            file: photoFile,
          },
          true
        );
        toast({
          title: 'Upload photo Successfully!',
        });
        return imageUrl;
      } catch (error: any) {
        console.log(error);
        toast({
          title: 'Uh oh! Something went wrong.',
          variant: 'destructive',
        });
      }
    },
    discover: async (id: string) => {
      try {
        const undiscoveredUsers = await fetch.get(
          `${API_URL}/api/v1/users/${id}/fetch-random-5-unmatched`
        );
        userStore.setState({ undiscoveredUsers: undiscoveredUsers });
        return undiscoveredUsers;
      } catch (error: any) {
        console.log(error);
      }
    },
    create_match_history: async (id1: string, id2: string, action: string) => {
      try {
        return await fetch.post(`${API_URL}/api/v1/match/add-match-history`, {
          senderId: id1,
          receiverId: id2,
          behavior: action,
        });
      } catch (error: any) {
        console.log(error);
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
export interface IUser {
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
  update: (id: string, params: any) => Promise<IUser>;
  upload: (id: string, name: string, photoFile: File) => Promise<string>;
  // delete: (id: string) => Promise<void>;
  setUser: (user: IUser) => Promise<void>;
  discover: (id: string) => Promise<IUser[]>;
  create_match_history: (
    id1: string,
    id2: string,
    action: string
  ) => Promise<void>;
}
