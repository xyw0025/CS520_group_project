import Link from 'next/link';
import React from 'react';
import { Button } from '@/components/ui/button';

const Main = () => {
  return (
    <div className=" w-full h-screen text-center">
      <div className="max-w-[1240px] h-full mx-auto p-2 place-self-center w-1/2">
        <div>
          <h1 className="font-bold text-lg md:text-xl lg:text-3xl py-10 text-gray-700 dark:text-white">
            <span className="text-[rgb(92,28,29)] dark:text-[rgb(228,89,89)]">
              UMassenger
            </span>
            , Connect students together{' '}
          </h1>
          <h1 className="text-lg md:text-xl lg:text-2xl py-10 text-gray-700 dark:text-white">
            This is a project based on CS520 <br />
            at UMass Amherst
          </h1>

          <Link href="/">
            <Button
              className="mx-auto place-self-center w-full lg:w-1/3"
              variant={'umass'}
            >
              Try it out!
            </Button>
          </Link>
        </div>
      </div>
    </div>
  );
};

export default Main;
