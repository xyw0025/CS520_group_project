import Link from 'next/link';
import React from 'react';

const Demo = () => {
  return (
    <div id="home" className=" w-full h-screen text-center">
      <div className="max-w-[1240px] w-full h-full mx-auto p-2 flex justify-center items-center">
        <div>
          <h1 className="py-4 text-gray-700 dark:text-white">
            <span className="text-[rgb(92,28,29)] dark:text-[rgb(228,89,89)]">
              UMassenger
            </span>
            , This is the Demo page
          </h1>

          <Link href="/register">
            <div className="rounded-full bg-[rgb(228,89,89)] shadow-lg shadow-gray-400 p-6 cursor-pointer hover:scale-110 ease-in duration-300">
              Register Now!
            </div>
          </Link>
        </div>
      </div>
    </div>
  );
};

export default Demo;
