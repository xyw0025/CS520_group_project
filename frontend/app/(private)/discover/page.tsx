'use client';

import { useState, useEffect } from 'react';
import { useUserService } from '@/utils';
import ProfileCard from '@/components/ProfileCard';

const Discover = () => {
  const userService = useUserService();
  const currentUser = userService.currentUser;
  const [undiscoveredUsers, setUndiscoveredUsers] = useState(
    userService.undiscoveredUsers
  );
  const [currentIndex, setCurrentIndex] = useState(userService.discoverIndex);

  // Todo: Should store in backend
  useEffect(() => {
    const savedIndex = localStorage.getItem('discoverIndex');
    if (savedIndex) {
      setCurrentIndex(parseInt(savedIndex, 0));
    }
  }, []);

  useEffect(() => {
    localStorage.setItem('discoverIndex', currentIndex.toString());
  }, [currentIndex]);

  useEffect(() => {
    // Function to fetch undiscovered users
    const fetchUndiscoveredUsers = async () => {
      try {
        if (currentUser && currentUser.id) {
          const fetchedUsers = await userService.discover(currentUser.id);
          setUndiscoveredUsers(fetchedUsers);
        }
      } catch (error) {
        console.error('Error fetching undiscovered users:', error);
      }
    };

    // Fetch undiscovered users if the array is empty
    if (!undiscoveredUsers || currentIndex === 5) {
      fetchUndiscoveredUsers();
      setCurrentIndex(0);
    }
  }, [currentUser, currentIndex]);

  const handleLikeDislike = async (id1: any, id2: any, action: string) => {
    // Replace 'apiEndpoint' with your API endpoint and 'action' with like/dislike
    try {
      await userService.create_match_history(id1, id2, action);
      // Move to the next user
      setCurrentIndex((prevIndex) => prevIndex + 1);
      userService.setDiscoverIndex(currentIndex);
    } catch (error) {
      console.error(`Error on ${action} action:`, error);
    }
  };

  return (
    <div className="grid grid-cols-5 gap-2">
      <div className="col-start-2 col-span-3 mt-10">
        {undiscoveredUsers && undiscoveredUsers[currentIndex] && (
          <ProfileCard
            key={undiscoveredUsers[currentIndex].id}
            user={undiscoveredUsers[currentIndex]}
            onLike={() =>
              handleLikeDislike(
                currentUser?.id,
                undiscoveredUsers[currentIndex].id,
                'ACCEPT'
              )
            }
            onDislike={() =>
              handleLikeDislike(
                currentUser?.id,
                undiscoveredUsers[currentIndex].id,
                'REJECT'
              )
            }
          />
        )}
      </div>
    </div>
  );
};

export default Discover;
