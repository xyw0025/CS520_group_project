import React from 'react';
import { Button } from './ui/button';

type HobbyButtonProps = {
  hobby: string;
  selectedHobbies: string[];
  toggleHobby: (hobby: string) => void;
};

const HobbyButton: React.FC<HobbyButtonProps> = ({
  hobby,
  selectedHobbies,
  toggleHobby,
}) => {
  const isSelected = selectedHobbies.includes(hobby);

  return (
    <Button
      type="button"
      variant={isSelected ? 'selected' : 'unselected'}
      onClick={(event) => {
        toggleHobby(hobby);
      }}
    >
      {hobby}
    </Button>
  );
};

export default HobbyButton;
