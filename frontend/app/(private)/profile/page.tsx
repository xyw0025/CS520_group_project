'use client';

import Link from 'next/link';
import { useEffect } from 'react';
import { Button } from '@/components/ui/button';
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
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';

const Profile = () => {
  const userService = useUserService();
  const currentUser = userService.currentUser;
  const {
    register,
    getValues,
    handleSubmit,
    formState,
    watch,
    setValue,
    clearErrors,
  } = useForm();
  const { errors } = formState;
  const genderValue = watch('gender');

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
    birthday: register('birth', {
      required: 'Your birth is required',
    }),
    // school: register('school', {
    //   required: 'Your birth is required',
    // }),
    // major: register('major'),
    // hobbies: register('hobbies'),
    // description: register('description', {
    //   maxLength: {
    //     value: 50,
    //     message: 'Username must be less than 50 characters',
    //   },
    // }),
  };

  async function onSubmit({ username, gender }: any) {
    console.log(username, gender);
    // await userService.login(email, password);
  }

  return (
    <div id="profile" className=" w-full h-screen">
      <div className="grid grid-cols-2 gap-2 w-full h-full mx-auto p-2  justify-center ">
        <Card>
          <CardHeader className="space-y-1">
            <CardTitle className="text-2xl">
              Hello, {currentUser?.email}
            </CardTitle>
            <CardDescription>Here is your profile!</CardDescription>
          </CardHeader>
          <form id="profile_form" onSubmit={handleSubmit(onSubmit)}>
            <CardContent className="grid gap-4">
              <div className="grid grid-cols-3 gap-2">
                <Label className="text-center text-xl" htmlFor="username">
                  Username
                </Label>
                <Input
                  {...fields.username}
                  id="username"
                  type="text"
                  placeholder="username"
                  onChange={() => clearErrors()}
                />
                <div className="text-red-700 font-bold">
                  {errors.username?.message?.toString()}
                </div>
              </div>
              <div className="grid grid-cols-3 gap-2">
                <Label className="text-center text-xl" htmlFor="gender">
                  Gender
                </Label>

                <select {...fields.gender}>
                  <option value="male">male</option>
                  <option value="female">female</option>
                  <option value="other">other</option>
                </select>

                <div className="text-red-700 font-bold">
                  {errors.gender?.message?.toString()}
                </div>
              </div>

              <div className="grid grid-cols-3 gap-2">
                <Label className="text-center text-xl" htmlFor="birthday">
                  Birthday
                </Label>
                <Input
                  {...fields.birthday}
                  id="birth"
                  type="text"
                  placeholder="username"
                  onChange={() => clearErrors()}
                />
                <div className="text-red-700 font-bold">
                  {errors.username?.message?.toString()}
                </div>
              </div>
            </CardContent>
            <CardFooter>
              <Button
                type="submit"
                disabled={formState.isSubmitting}
                className="mx-auto place-self-center w-1/2"
                variant={'umass'}
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
    </div>
  );
};

export default Profile;
