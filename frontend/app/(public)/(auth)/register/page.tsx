'use client';

import { useState, useEffect } from 'react';
import { Icons } from '@/components/icons';
import { Button } from '@/components/ui/button';
import LoadingBox from '@/components/LoadingBox';
import { useForm } from 'react-hook-form';
import { useUserService } from 'utils';
import { useRouter } from 'next/navigation';
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
import { PasswordInput } from '@/components/PasswordInput';

export default function Register() {
  const userService = useUserService();

  // get functions to build form with useForm() hook
  const { register, getValues, handleSubmit, formState, clearErrors } =
    useForm();
  const { errors } = formState;
  const { currentUser } = userService;
  const router = useRouter();
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const storedUser = localStorage.getItem('currentUser');
    if (storedUser) {
      userService.setUser(JSON.parse(storedUser));
      router.replace('/');
    } else {
      setIsLoading(false);
    }
  }, []);

  const fields = {
    email: register('email', {
      required: 'Email is required',
      pattern: {
        value: /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Z|a-z]{2,}$/,
        message: 'Invalid email address',
      },
    }),
    password: {
      ...register('password', {
        required: 'Password is required',
        minLength: {
          value: 8,
          message: 'Password must be at least 8 characters',
        },
        maxLength: {
          value: 20,
          message: 'Password must be less than 20 characters',
        },
      }),
      errorMessage: errors.password?.message,
    },
    confirmPassword: {
      ...register('confirmPassword', {
        required: 'Confirm Password is required',
        minLength: {
          value: 8,
          message: 'Confirm Password must be at least 8 characters',
        },
        maxLength: {
          value: 20,
          message: 'Confirm Password must be less than 20 characters',
        },
        validate: (value) => {
          const passwordValue = getValues('password');
          if (value !== passwordValue) {
            return 'Passwords do not match';
          }
        },
      }),
      errorMessage: errors.confirmPassword?.message,
    },
  };

  async function onSubmit(user: any) {
    await userService.register(user);
  }

  if (isLoading) {
    return (
      <div className="container mx-auto py-32">
        <LoadingBox />
      </div>
    );
  }

  return (
    <div className="container mx-auto py-32">
      <Card>
        <CardHeader className="space-y-1">
          <CardTitle className="text-2xl">Create an account</CardTitle>
          <CardDescription>
            Enter your email below to create your account
          </CardDescription>
        </CardHeader>
        <form onSubmit={handleSubmit(onSubmit)}>
          <CardContent className="grid gap-4">
            {/* <div className="grid grid-cols-1 gap-6">
              <Button variant="outline">
                <Icons.google className="mr-2 h-4 w-4" />
                Google
              </Button>
            </div>
            <div className="relative">
              <div className="absolute inset-0 flex items-center">
                <span className="w-full border-t" />
              </div>
              <div className="relative flex justify-center text-xs uppercase">
                <span className="bg-background px-2 text-muted-foreground">
                  Or continue with
                </span>
              </div>
            </div> */}
            <div className="grid gap-2">
              <Label htmlFor="email">Email</Label>
              <Input
                {...fields.email}
                type="text"
                placeholder="ID@umass.edu"
                onChange={() => clearErrors()}
              />
              <div className="text-red-700 font-bold">
                {errors.email?.message?.toString()}
              </div>
            </div>
            <div className="grid gap-2">
              <Label htmlFor="password">Password</Label>
              <PasswordInput fields={ fields.password } />
              <div className="text-red-700 font-bold">
                {errors.password?.message?.toString()}
              </div>
            </div>
            <div className="grid gap-2">
              <Label htmlFor="confirmPassword">Confirm Password</Label>
              <PasswordInput placeholder='enter your password again' fields={ fields.confirmPassword } />
              <div className="text-red-700 font-bold">
                {errors.confirmPassword?.message?.toString()}
              </div>
            </div>
            <Button
              type="submit"
              disabled={formState.isSubmitting}
              className="mx-auto place-self-center w-1/2"
              variant={'umass'}
            >
              {formState.isSubmitting && (
                <span className="spinner-border spinner-border-sm me-1"></span>
              )}
              Create account
            </Button>
          </CardContent>
        </form>
      </Card>
    </div>
  );
}
