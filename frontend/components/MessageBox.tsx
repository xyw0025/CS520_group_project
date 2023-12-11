import React from 'react';

interface MessageProps {
  text: string;
  isSender: boolean;
  imageUrl: string;
  createdAt: string;
}

const MessageBox: React.FC<MessageProps> = ({ text, isSender, imageUrl }) => {
  return isSender ? (
    <div className="flex justify-end mb-4">
      <div className="mr-2 py-3 px-4 bg-blue-400 rounded-bl-3xl rounded-tl-3xl rounded-tr-xl text-white">
        {text}
      </div>
      <img
        src={imageUrl}
        className="object-cover h-8 w-8 rounded-full"
        alt=""
      />
    </div>
  ) : (
    <div className="flex justify-start mb-4">
      <img
        src={imageUrl}
        className="object-cover h-8 w-8 rounded-full"
        alt=""
      />
      <div className="ml-2 py-3 px-4 bg-gray-400 rounded-br-3xl rounded-tr-3xl rounded-tl-xl text-white">
        {text}
      </div>
    </div>
  );
};

export default MessageBox;
