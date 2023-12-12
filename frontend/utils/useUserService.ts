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
  discoverIndex: 0,
  currentUser: undefined,
};
const userStore = create<IUserStore>(() => initialState);

function useUserService(): IUserService {
  const fetch = useFetch();
  const router = useRouter();
  const searchParams = useSearchParams();
  const { toast } = useToast();
  const { matchedUsers, undiscoveredUsers, discoverIndex, currentUser } =
    userStore();
  const API_URL = process.env.NEXT_PUBLIC_API_URL;

  return {
    matchedUsers,
    undiscoveredUsers,
    discoverIndex,
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
        const returnUrl = searchParams.get('returnUrl') || '/profile';
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
    getMatchedUsers: async (id) => {
      if (currentUser) {
        const userWithConversationData = await fetch.get(
          `${API_URL}/api/v1/match/get-all-matched-users/${id}`
        );
        userStore.setState((state) => ({
          ...state,
          matchedUsers: userWithConversationData,
        }));
        return userWithConversationData;
      }
      return [];
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
    setDiscoverIndex: async (index) => {
      try {
        userStore.setState({ discoverIndex: index });
      } catch (error: any) {
        toast({
          title: 'Uh oh! Something went wrong.',
          description: error,
          variant: 'destructive',
        });
      }
    },
    create_match_history: async (id1: string, id2: string, action: string) => {
      try {
        const match = await fetch.post(
          `${API_URL}/api/v1/match/add-match-history`,
          {
            senderId: id1,
            receiverId: id2,
            behavior: action,
          }
        );
        if (match && match?.status == 1) {
          const newMatchedUser = await fetch.get(
            `${API_URL}/api/v1/users/${id2}`
          );
          userStore.setState((state) => ({
            ...state,
            matchedUsers: [...(state.matchedUsers || []), newMatchedUser],
          }));
          toast({
            title: 'Congratulation!',
            description: ' Successfully Match!',
          });
        }
      } catch (error: any) {
        console.log(error);
      }
    },
    getConversation: async (id1: string, id2: string) => {
      try {
        return await fetch.get(
          `${API_URL}/api/v1/conversation/messages?user1Id=${id1}&user2Id=${id2}`
        );
      } catch (error: any) {
        console.log(error);
      }
    },
    getConversationId: async (id1: string, id2: string) => {
      try {
        return await fetch.get(
          `${API_URL}/api/v1/conversation/id?user1Id=${id1}&user2Id=${id2}`
        );
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

export interface Message {
  senderId?: string;
  receiverId?: string;
  messageText: string;
  createdAt: string;
}

interface IUserStore {
  matchedUsers?: UserWithConversationData[];
  undiscoveredUsers?: IUser[];
  discoverIndex: number;
  currentUser?: IUser;
}

export interface UserWithConversationData {
  id: string;
  profile?: Profile;
  lastMessage?: Message;
  unreadCount: number;
}

interface IUserService extends IUserStore {
  login: (username: string, password: string) => Promise<void>;
  logout: () => Promise<void>;
  register: (user: IUser) => Promise<void>;
  getCurrent: () => Promise<IUser>;
  getMatchedUsers: (id: string) => Promise<UserWithConversationData[]>;
  getConversation: (id1: string, id2: string) => Promise<Message[]>;
  getConversationId: (id1: string, id2: string) => Promise<string>;
  update: (id: string, params: any) => Promise<IUser>;
  upload: (id: string, name: string, photoFile: File) => Promise<string>;
  // delete: (id: string) => Promise<void>;
  setUser: (user: IUser) => Promise<void>;
  discover: (id: string) => Promise<IUser[]>;
  setDiscoverIndex: (index: number) => void;
  create_match_history: (
    id1: string,
    id2: string,
    action: string
  ) => Promise<void>;
}
