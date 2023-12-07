'use client';

import { useState } from 'react';
import { useUserService } from '@/utils';
import { Button } from './ui/button';
import { Separator } from '@/components/ui/separator';
import { Card, CardContent, CardFooter } from '@/components/ui/card';
import { HeartFilledIcon, Cross2Icon } from '@radix-ui/react-icons';
import Carousel from 'react-bootstrap/Carousel';
import { Photos } from '@/utils/photo';
import Image from 'next/image';
const ProfileCard = () => {
  const userService = useUserService();
  const { currentUser } = userService;
  const [photos, setPhotos] = useState(Photos);

  return (
    <Card className="bg-gradient-to-tl from-gray-100 to-gray-400 grid grid-cols-2 p-2 rounded-3xl dark:bg-gray-700 ">
      <CardContent className="mt-5 pt-3 bg-gray-500 ring-2 ring-gray-600 rounded-2xl">
        <Carousel>
          {photos.map((photoUrl, index) => (
            <Carousel.Item key={index}>
              <Image
                className="rounded-3xl"
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
        <p className="text-5xl font-bold font-serif">Po, 27</p>
        <Separator className="my-3 bg-black " />
        <p className="text-xl font-bold italic">
          Computer Science, UMass Amherst
        </p>
        <Separator className="my-3 bg-black " />
        <div>
          {[
            'Music',
            'Movies',
            'Painting',
            'Bar',
            'Foodie',
            'Cooking',
            'Reading',
            'Research',
            'Fitness',
            'Traveling',
            'Cycling',
            'Hiking',
          ].map((hobby) => (
            <Button type="button" variant="umass2" className="m-1 cursor-none">
              {hobby}
            </Button>
          ))}
        </div>
        <p className="text-xl mt-10 ml-2">I am Po.</p>
      </CardContent>
      <Separator className="col-span-2 my-3 bg-black " />

      <CardFooter className="col-span-2 justify-center mt-3">
        <Button
          type="button"
          variant="umass"
          className="mx-5 w-40 bg-blue-300 border-3 border-blue-500"
        >
          <Cross2Icon className="text-blue-600 scale-150" />
        </Button>
        <Button
          type="button"
          variant="umass"
          className="mx-5 w-40 bg-red-300 border-3 border-red-500 shadow-2xl"
        >
          <HeartFilledIcon className="text-red-600 scale-150 " />
        </Button>
      </CardFooter>
    </Card>
  );
};

export default ProfileCard;
