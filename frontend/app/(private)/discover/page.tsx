'use client';

import { useUserService } from '@/utils';
import ProfileCard from '@/components/ProfileCard';

const Dashboard = () => {
  const userService = useUserService();
  const currentUser = userService.currentUser;

  return (
    <div className="grid grid-cols-5 gap-2">
      <div className="col-start-2 col-span-3 mt-10">
        <ProfileCard />
      </div>
    </div>
  );
};

export default Dashboard;
