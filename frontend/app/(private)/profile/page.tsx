'use client';

import HobbyButton from '@/components/HobbyButton';
import { useState, useEffect, useRef } from 'react';
import { Button } from '@/components/ui/button';
import { Textarea } from '@/components/ui/textarea';
import { useUserService } from '@/utils';
import { useForm } from 'react-hook-form';
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from '@/components/ui/card';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import ProfilePhotos from '@/components/ProfilePhotos';

const Profile = () => {
  const userService = useUserService();
  const currentUser = userService.currentUser;
  const { register, handleSubmit, formState, clearErrors, reset } = useForm();
  const { errors } = formState;

  const MAX_HOBBIES = 5;
  const [selectedHobbies, setSelectedHobbies] = useState(
    currentUser?.profile?.preferences?.map((preference) => preference.name) ||
      []
  );
  const [hobbiesAlert, setHobbiesAlert] = useState(false);
  const toggleHobby = (hobby: string) => {
    setSelectedHobbies((prevHobbies: any) => {
      if (prevHobbies.length >= MAX_HOBBIES && !prevHobbies.includes(hobby)) {
        setHobbiesAlert(true);
        return prevHobbies;
      }
      setHobbiesAlert(false);
      return prevHobbies.includes(hobby)
        ? prevHobbies.filter((h: any) => h !== hobby)
        : [...prevHobbies, hobby];
    });
  };

  useEffect(() => {
    async function fetchUserAndResetForm() {
      try {
        const user = await userService.getCurrent();

        reset({
          username: user.profile?.displayName || '',
          gender: user.profile?.gender || 'Male',
          birthday: user.profile?.birthday || '',
          major: user.profile?.major || '',
          introduction: user.profile?.bio || '',
        });
      } catch (error) {
        console.error('Error fetching user:', error);
      }
    }

    fetchUserAndResetForm();
  }, []);

  const fields = {
    username: register('username', {
      required: 'Username is required',
      maxLength: {
        value: 50,
        message: 'Username must be less than 50 characters',
      },
    }),
    gender: register('gender', {
      required: 'Gender is required',
    }),
    birthday: register('birthday', {
      required: 'Your birth is required',
    }),
    major: register('major'),

    introduction: register('introduction', {
      maxLength: {
        value: 50,
        message: 'Self-Introduction must be less than 500 characters',
      },
    }),
  };

  async function onSubmit({
    username,
    gender,
    major,
    birthday,
    introduction,
  }: any) {
    if (currentUser && currentUser.id) {
      const updatedUser = await userService.update(currentUser.id, {
        displayName: username,
        gender,
        birthday,
        major,
        bio: introduction,
        imageUrls: currentUser.profile?.imageUrls,
        preferences: selectedHobbies,
      });
      userService.setUser(updatedUser);
    } else {
      console.log('Can not get the currentUser');
    }
  }

  return (
    <div id="profile" className=" w-full h-screen">
      <Card className="m-10">
        <CardHeader>
          <CardTitle className="text-2xl">
            Hello, {currentUser?.email}
          </CardTitle>
          <CardDescription>Here is your profile!</CardDescription>
        </CardHeader>
        <form id="profile_form" onSubmit={handleSubmit(onSubmit)}>
          <CardContent className="grid grid-cols-2 gap-2 w-full h-full mx-auto p-2 justify-center">
            <div>
              <div className="grid grid-cols-3 gap-2">
                <Label className="text-center text-xl p-2" htmlFor="username">
                  Username
                </Label>
                <Input
                  {...fields.username}
                  id="username"
                  type="text"
                  placeholder="Username"
                  onChange={() => clearErrors()}
                />
                <div className="text-red-700 font-bold">
                  {errors.username?.message?.toString()}
                </div>
              </div>
              <div className="grid grid-cols-3 gap-2 p-2">
                <Label className="text-center text-xl" htmlFor="gender">
                  Gender
                </Label>

                <select {...fields.gender}>
                  <option value="Male">Male</option>
                  <option value="Female">Female</option>
                  <option value="Other">Non-Binary</option>
                </select>

                <div className="text-red-700 font-bold">
                  {errors.gender?.message?.toString()}
                </div>
              </div>

              <div className="grid grid-cols-3 gap-2 p-2">
                <Label className="text-center text-xl" htmlFor="birthday">
                  Birthday
                </Label>
                <Input
                  {...fields.birthday}
                  id="birthday"
                  type="date"
                  placeholder="birthday"
                  onChange={() => clearErrors()}
                />
                <div className="text-red-700 font-bold">
                  {errors.birthday?.message?.toString()}
                </div>
              </div>

              <div className="grid grid-cols-3 gap-2 p-2">
                <Label className="text-center text-xl" htmlFor="major">
                  Major
                </Label>
                <Input
                  {...fields.major}
                  id="major"
                  type="text"
                  placeholder="Major"
                  onChange={() => clearErrors()}
                />
                <div className="text-red-700 font-bold">
                  {errors.major?.message?.toString()}
                </div>
              </div>
              {/* Hobbies */}
              <div className="grid grid-cols-3 gap-2">
                <Label className="text-center text-xl" htmlFor="hobbies">
                  Hobbies
                </Label>
                <div className="col-span-2">
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
                    <HobbyButton
                      key={hobby}
                      hobby={hobby}
                      selectedHobbies={selectedHobbies}
                      toggleHobby={toggleHobby}
                    />
                  ))}
                  <div className="text-red-700 font-bold">
                    {hobbiesAlert && 'You can select up to 5 hobbies.'}
                  </div>
                </div>
                {/* Self-Introduction */}
                <Label className="text-center text-xl" htmlFor="introduction">
                  Self-Introduction
                </Label>
                <div className="col-span-2">
                  <div className="grid w-full gap-1.5">
                    <Textarea
                      {...fields.introduction}
                      id="introduction"
                      placeholder="Introduce yourself briefly - start your journey here!"
                      onChange={() => clearErrors()}
                      rows={15}
                    />
                    <div className="text-red-700 font-bold">
                      {errors.introduction?.message?.toString()}
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <ProfilePhotos />
          </CardContent>

          <CardFooter>
            <Button
              type="submit"
              disabled={formState.isSubmitting}
              className="mx-auto place-self-center w-1/2"
              variant={'umass2'}
            >
              {formState.isSubmitting && (
                <span className="spinner-border spinner-border-sm me-1"></span>
              )}
              Confirm!
            </Button>
          </CardFooter>
        </form>
      </Card>
    </div>
  );
};

export default Profile;
