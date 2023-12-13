import React from 'react';
import moment from 'moment';

interface MessageProps {
  text: string;
  isSender: boolean;
  imageUrl: string;
  createdAt: string;
  showDate?: string;
}
const MessageBox: React.FC<MessageProps> = ({
  text,
  isSender,
  imageUrl,
  createdAt,
  showDate,
}) => {
  const formattedTime = moment(createdAt).format('LT');

  return (
    <div>
      {showDate && (
        <div className="text-center text-xs text-gray-500">{showDate}</div>
      )}{' '}
      {isSender ? (
        <div className="flex justify-end mb-4">
          <span className="flex flex-col-reverse text-xs text-gray-500">
            {formattedTime}
          </span>
          <div className="mr-2 py-3 px-4 bg-blue-400 rounded-bl-3xl rounded-tl-3xl rounded-tr-xl text-white relative">
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
          <div className="ml-2 py-3 px-4 bg-gray-400 rounded-br-3xl rounded-tr-3xl rounded-tl-xl text-white relative">
            {text}
          </div>
          <span className="flex flex-col-reverse text-xs text-gray-500">
            {formattedTime}
          </span>
        </div>
      )}
    </div>
  );
};

export default MessageBox;
