import React, { useState } from 'react';
import { Input } from '@/components/ui/input';
import { useForm } from 'react-hook-form';

type PasswordInputProps = {
    fields: {
        password: {}
    };
    placeholder?: string;
};

const PasswordInput = ({
    fields,
    placeholder
}: PasswordInputProps) => {
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const { clearErrors } = useForm();

  const togglePasswordVisibility = () => {
    setShowPassword((prev) => !prev);
  };


  return (
    <div className="relative">
      <Input
        {...fields.password}
        id="password"
        type={showPassword ? "text" : "password"}
        value={password}
        onChange={(e) => {
            setPassword(e.target.value);
            clearErrors();
        }}
        placeholder={placeholder}
      />
      {password && (
        <span
          className="absolute cursor-pointer"
          onClick={togglePasswordVisibility}
          style={{ right: '10px', top: '50%', transform: 'translateY(-50%)' }}
          placeholder={placeholder}
        >
          {showPassword ? 'hide' : 'show'}
        </span>
      )}
    </div>
  );
}

export { PasswordInput };
