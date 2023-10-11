import Link from 'next/link';
import React from 'react';

const Main = () => {
  return (
    <div id="home" className=" w-full h-screen text-center">
      <div className="max-w-[1240px] w-full h-full mx-auto p-2 flex justify-center items-center">
        <div>
          <h1 className="py-4 text-gray-700 dark:text-white">
            <span className="text-[rgb(92,28,29)] dark:text-[rgb(228,89,89)]">
              UMassenger
            </span>
            , Connect our students together{' '}
          </h1>
          <h1 className="py-10 text-gray-700 dark:text-white">
            This is a project based on CS520 <br />
            at UMass Amherst
          </h1>

          <Link href="/">
            <div className="rounded-full bg-[rgb(228,89,89)] shadow-lg shadow-gray-400 p-6 cursor-pointer hover:scale-110 ease-in duration-300">
              Try it out!
            </div>
          </Link>
        </div>
      </div>
    </div>
  );
};

export default Main;
