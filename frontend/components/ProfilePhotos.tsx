'use client';

import { useState, useEffect } from 'react';
import { useUserService } from '@/utils';
import PhotoCard from './PhotoCard';
import BlankPhotoCard from './BlankPhotoCard';

import { Photos } from '@/data';

const ProfilePhotos = () => {
  const userService = useUserService();
  const { currentUser } = userService;
  const [photos, setPhotos] = useState(Photos);
  const [blankPhotoCounter, setblankPhotoCounter] = useState(photos.length);

  const handleRemovePhoto = (photoName: string) => {
    setPhotos((prevPhotos) =>
      prevPhotos.filter((photo) => photo.name !== photoName)
    );
  };

  const handleAddPhoto = () => {
    // Implement your logic to add a photo here
    console.log('Add photo logic goes here');
    // For example, show a file input dialog or navigate to a photo upload screen
  };

  useEffect(() => {
    setblankPhotoCounter(Math.max(0, 4 - photos.length));
  }, [handleRemovePhoto]);

  return (
    <div className="grid grid-cols-2 gap-2 w-full h-full mx-auto p-2 justify-items-center items-start">
      {photos.map((photo) => (
        <PhotoCard
          key={photo.name}
          photo={photo}
          onRemove={handleRemovePhoto}
          className="w-[300px]"
          aspectRatio="portrait"
          width={750}
          height={750}
        />
      ))}

      {/* Render BlankPhotoCards as placeholders */}
      {[...Array(blankPhotoCounter)].map((_, index) => (
        <PhotoCard
          key={`placeholder-${index}`}
          isPlaceholder
          onAdd={handleAddPhoto}
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
