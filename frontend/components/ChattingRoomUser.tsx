'use client';

import { IUser } from '@/utils';
import Image from 'next/image';

interface ChattingRoomUserProps {
  user: IUser;
  onClick: () => void;
  isSelected: boolean;
}

const ChattingRoomUser: React.FC<ChattingRoomUserProps> = ({
  user,
  onClick,
  isSelected,
}) => {
  return (
    <div
      className={`flex flex-row py-4 px-2 justify-center items-center border-b-2 ${
        isSelected ? 'bg-blue-100' : ''
      }`}
      onClick={onClick}
    >
      <div className="w-1/4">
        {user?.profile?.imageUrls && (
          <Image
            className={`rounded-full object-cover w-12 h-12 ${
              isSelected ? 'ring-2 ring-blue-500' : ''
            }`}
            src={user.profile.imageUrls[0]}
            alt="Photo"
            width={48}
            height={48}
          />
        )}
      </div>
      <div className="w-full">
        <div
          className={`text-lg font-semibold ${
            isSelected ? 'font-extrabold' : ''
          }`}
        >
          {user?.profile?.displayName}
        </div>
        <span className="text-gray-500">Pick me at 9:00 AM</span>
      </div>
    </div>
  );
};

export default ChattingRoomUser;
