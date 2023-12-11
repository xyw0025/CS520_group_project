'use client';

import { IUser } from '@/utils';
import Image from 'next/image';

interface ProfileCardProps {
  user: IUser;
}

const ChattingRoomUser: React.FC<ProfileCardProps> = ({ user }) => {
  return (
    <div className="flex flex-row py-4 px-2 justify-center items-center border-b-2">
      <div className="w-1/4">
        {user?.profile?.imageUrls && (
          <Image
            className="rounded-full object-cover w-12 h-12"
            src={user?.profile?.imageUrls[0]}
            alt="Photo"
            width={100}
            height={100}
          />
        )}
      </div>
      <div className="w-full">
        <div className="text-lg font-semibold">
          {user?.profile?.displayName}
        </div>
        <span className="text-gray-500">Pick me at 9:00 Am</span>
      </div>
    </div>
  );
};

export default ChattingRoomUser;
