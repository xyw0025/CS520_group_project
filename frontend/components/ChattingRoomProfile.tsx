'use client';

import { UserWithConversationData } from '@/utils';
import { Button } from './ui/button';
import { Separator } from '@/components/ui/separator';
import { Card, CardContent, CardFooter } from '@/components/ui/card';
import Carousel from 'react-bootstrap/Carousel';
import Image from 'next/image';

interface ProfileCardProps {
  user: UserWithConversationData;
}
const ChattingRoomProfile: React.FC<ProfileCardProps> = ({ user }) => {
  return (
    <div className=" flex-col p-2 rounded-3xl dark:bg-gray-700  w-full">
      <div className="w-full h-200 mt-5 pt-3 bg-gray-200 rounded-2xl overflow-hidden">
        <Carousel className="overflow-hidden" interval={100000}>
          {user?.profile?.imageUrls &&
            user?.profile?.imageUrls.map((photoUrl: string, index) => (
              <Carousel.Item
                key={photoUrl + index}
                className="overflow-hidden "
              >
                <Image
                  key={photoUrl}
                  className="rounded-3xl overflow-hidden"
                  src={photoUrl}
                  alt="Photo"
                  width={300}
                  height={300}
                />
              </Carousel.Item>
            ))}
        </Carousel>
      </div>

      <div className="mt-2">
        <p className="text-3xl font-bold font-serif">
          {user?.profile?.displayName}, {user?.profile?.age}
        </p>
        <Separator className="my-3 bg-black " />
        <p className="text-l font-bold italic">
          {user?.profile?.major}, UMass Amherst
        </p>
        {user?.profile?.preferences && <Separator className="my-1 bg-black " />}
        <div>
          {user?.profile?.preferences?.map((preference) => (
            <Button
              key={preference.name}
              type="button"
              variant="umass2"
              className="m-1 cursor-none"
            >
              {preference.name}
            </Button>
          ))}
        </div>
        {user?.profile?.bio && (
          <Separator className="col-span-2 my-3 bg-black " />
        )}
        <p className="text-l mt-1 ml-2">{user?.profile?.bio}</p>
      </div>
    </div>
  );
};

export default ChattingRoomProfile;
