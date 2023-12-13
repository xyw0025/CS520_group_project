'use client';

import { useState } from 'react';
import { IUser } from '@/utils';
import { Button } from './ui/button';
import { Separator } from '@/components/ui/separator';
import { Card, CardContent, CardFooter } from '@/components/ui/card';
import { HeartFilledIcon, Cross2Icon } from '@radix-ui/react-icons';
import Carousel from 'react-bootstrap/Carousel';
import Image from 'next/image';

interface ProfileCardProps {
  user: IUser;
  onLike: () => void;
  onDislike: () => void;
}

const ProfileCard: React.FC<ProfileCardProps> = ({
  user,
  onLike,
  onDislike,
}) => {
  const [isSubmitting, setIsSubmitting] = useState(false);
  const handleLike = async () => {
    setIsSubmitting(true);
    try {
      await onLike();
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleDislike = async () => {
    setIsSubmitting(true);
    try {
      await onDislike();
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <Card className="h-1000 bg-gradient-to-tl from-gray-100 to-gray-400 grid grid-cols-2 p-2 rounded-3xl dark:bg-gray-700 h-500 w-500 ">
      <CardContent className="h-600 w-500 mt-5 pt-3 bg-gray-500 ring-2 ring-gray-600 rounded-2xl overflow-hidden">
        <Carousel className="overflow-hidden">
          {user?.profile?.imageUrls &&
            user?.profile?.imageUrls.map((photoUrl: string) => (
              <Carousel.Item
                key="photo"
                className="h-500 w-500 overflow-hidden"
              >
                <Image
                  className="rounded-3xl overflow-hidden"
                  src={photoUrl}
                  alt="Photo"
                  width={500}
                  height={500}
                />
              </Carousel.Item>
            ))}
        </Carousel>
      </CardContent>

      <CardContent className="mt-32">
        <p className="text-5xl font-bold font-serif">
          {user?.profile?.displayName}, {user?.profile?.age}
        </p>
        <Separator className="my-3 bg-black " />
        <p className="text-xl font-bold italic">
          {user?.profile?.major}, UMass Amherst
        </p>
        <Separator className="my-3 bg-black " />
        <div>
          {user?.profile?.preferences?.map((preference) => (
            <Button type="button" variant="umass2" className="m-1 cursor-none">
              {preference.name}
            </Button>
          ))}
        </div>
        <p className="text-xl mt-10 ml-2">{user?.profile?.bio}</p>
      </CardContent>
      <Separator className="col-span-2 my-3 bg-black " />

      <CardFooter className="col-span-2 justify-center mt-3">
        <Button
          type="button"
          disabled={isSubmitting}
          variant="umass"
          className="mx-5 w-40 bg-blue-300 border-3 border-blue-500"
          onClick={handleDislike}
        >
          <Cross2Icon className="text-blue-600 scale-150" />
        </Button>
        <Button
          type="button"
          disabled={isSubmitting}
          variant="umass"
          className="mx-5 w-40 bg-red-300 border-3 border-red-500 shadow-2xl"
          onClick={handleLike}
        >
          <HeartFilledIcon className="text-red-600 scale-150 " />
        </Button>
      </CardFooter>
    </Card>
  );
};

export default ProfileCard;
