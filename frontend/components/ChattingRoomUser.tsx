'use client';

import { UserWithConversationData } from '@/utils';
import Image from 'next/image';

interface ChattingRoomUserProps {
  user: UserWithConversationData;
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
        <div className="grid grid-cols-8">
          <span className="col-span-7 text-gray-500">
            {user.lastMessage?.messageText}
          </span>
          {user.unreadCount > 0 && (
            <div className=" flex items-center justify-center bg-blue-500 text-white rounded-full w-6 h-6">
              {user.unreadCount}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default ChattingRoomUser;
