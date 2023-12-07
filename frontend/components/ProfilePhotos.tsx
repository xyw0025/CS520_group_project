'use client';

import { useState, useEffect, useRef } from 'react';
import { useUserService } from '@/utils';
import PhotoCard from './PhotoCard';

const ProfilePhotos = () => {
  const userService = useUserService();
  const { currentUser } = userService;
  const [photos, setPhotos] = useState(currentUser?.profile?.imageUrls || []);
  const [blankPhotoCounter, setBlankPhotoCounter] = useState(photos.length);

  function generateUniquePhotoName() {
    const timestamp = new Date().getTime();
    const randomPart = Math.floor(Math.random() * 10000);
    return `photo_${timestamp}_${randomPart}`;
  }

  const handleAddPhoto = async (photoFile: File) => {
    if (currentUser) {
      try {
        const newPhotoName = generateUniquePhotoName();
        const photoUrl = await userService.upload(
          currentUser.id,
          newPhotoName,
          photoFile
        );

        setPhotos((prevPhotos) => {
          const updatedPhotos = [...prevPhotos, photoUrl];

          if (currentUser && currentUser.profile) {
            // Update the currentUser with the new photos array
            const updatedUser = {
              ...currentUser,
              profile: {
                ...currentUser.profile,
                imageUrls: updatedPhotos,
              },
            };
            userService.setUser(updatedUser);
          }

          return updatedPhotos;
        });
      } catch (error) {
        console.error(error);
      }
    }
  };

  const handleRemovePhoto = (photoUrl: string) => {
    setPhotos((prevPhotos) => {
      const updatedPhotos = prevPhotos.filter((photo) => photo !== photoUrl);
      if (currentUser && currentUser.profile) {
        const updatedUser = {
          ...currentUser,
          profile: {
            ...currentUser.profile,
            imageUrls: updatedPhotos,
          },
        };
        userService.setUser(updatedUser);
      }
      return updatedPhotos;
    });
    console.log(currentUser);
  };

  const fileInputRef = useRef<HTMLInputElement>(null);

  const handleFileSelect = (event: React.ChangeEvent<HTMLInputElement>) => {
    if (event.target.files && event.target.files[0]) {
      const file = event.target.files[0];
      handleAddPhoto(file);
    }
  };

  const triggerFileInput = () => {
    if (fileInputRef && fileInputRef.current) {
      fileInputRef.current.click();
    }
  };

  useEffect(() => {
    setBlankPhotoCounter(Math.max(0, 4 - photos.length));
  }, [handleRemovePhoto, handleAddPhoto]);

  useEffect(() => {
    if (currentUser) {
      setPhotos(currentUser.profile?.imageUrls || []);
    }
  }, [currentUser]);

  return (
    <div className="grid grid-cols-2 gap-2 w-full h-full mx-auto p-2 justify-items-center items-start">
      {photos.map((photoUrl) => (
        <PhotoCard
          photoUrl={photoUrl}
          onRemove={handleRemovePhoto}
          className="w-[300px]"
          aspectRatio="portrait"
          width={750}
          height={750}
        />
      ))}

      {/* Render BlankPhotoCards as placeholders */}
      <input
        type="file"
        ref={fileInputRef}
        style={{ display: 'none' }}
        onChange={handleFileSelect}
      />
      {[...Array(blankPhotoCounter)].map((_, index) => (
        <PhotoCard
          key={`placeholder-${index}`}
          isPlaceholder
          onAdd={triggerFileInput}
          className="w-[300px]"
          aspectRatio="portrait"
          width={750}
          height={750}
        />
      ))}
    </div>
  );
};

export default ProfilePhotos;
