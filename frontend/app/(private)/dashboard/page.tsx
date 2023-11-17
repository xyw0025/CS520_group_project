'use client';

import { useEffect } from 'react';
import { useUserService } from '@/utils';

const Dashboard = () => {
  const userService = useUserService();
  const currentUser = userService.currentUser;

  useEffect(() => {
    userService.getCurrent();
  }, []);

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
            This is {currentUser?.email}'s dashboard <br />
            at UMass Amherst
          </h1>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
