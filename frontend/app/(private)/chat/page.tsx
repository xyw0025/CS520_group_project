'use client';

import { useState, useEffect } from 'react';
import { IUser } from '@/utils';
import { Textarea } from '@/components/ui/textarea';
import { useUserService } from '@/utils';
import { PaperPlaneIcon } from '@radix-ui/react-icons';

import ChattingRoomUser from '@/components/ChattingRoomUser';
import Message from '@/components/Message';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';

const Chat = () => {
  const userService = useUserService();
  const currentUser = userService.currentUser;
  const [matchedUsers, setMatchedUsers] = useState(userService.matchedUsers);
  const [currentChatUser, setCurrentChatUser] = useState<IUser | null>(null);

  // Connect to the WebSocket server
  const API_URL = process.env.NEXT_PUBLIC_API_URL;
  const socket = new SockJS(`${API_URL}/chat`);
  const stompClient = new Client({
    webSocketFactory: () => socket,
  });
  const [message, setMessage] = useState('');

  stompClient.onConnect = () => {
    console.log('Build WebSocket connection!');
  };

  stompClient.activate();

  function sendMessage(message: String) {
    const chatMessage = {
      senderId: currentUser?.id,
      receiverId: currentChatUser?.id,
      messageText: message,
    };

    stompClient.publish({
      destination: '/app/sendMessage', // The destination endpoint
      body: JSON.stringify(chatMessage), // Message body
    });
  }

  const handleUserSelect = (user: IUser) => {
    setCurrentChatUser(user);
  };

  useEffect(() => {
    // Function to fetch matched users
    const fetchMatchedUsers = async () => {
      try {
        if (currentUser && currentUser.id) {
          const fetchedUsers = await userService.getMatchedUsers(
            currentUser.id
          );
          setMatchedUsers(fetchedUsers);
        }
      } catch (error) {
        console.error('Error fetching matched users:', error);
      }
    };
    fetchMatchedUsers();
  }, []);

  const handleSendMessage = () => {
    sendMessage(message);
    setMessage('');
  };

  return (
    <div className="container mx-auto shadow-lg rounded-lg h-600">
      {/* Chatting */}
      <div className="flex flex-row justify-between bg-white">
        {/* chat list */}
        <div className="flex flex-col w-2/5 border-r-2 overflow-y-auto">
          {/* user list */}
          {matchedUsers &&
            matchedUsers.map((matchedUser) => (
              <ChattingRoomUser
                user={matchedUser}
                onClick={() => handleUserSelect(matchedUser)}
                isSelected={currentChatUser?.id === matchedUser.id}
              />
            ))}
          {/* end user list */}
        </div>
        {/* end chat list */}
        {/* message */}
        <div className="w-full px-5 flex flex-col justify-between">
          <div className="flex flex-col mt-5">
            <Message
              text="test"
              isSender={true}
              imageUrl={
                currentChatUser?.profile?.imageUrls?.[0] || 'defaultImageUrl'
              }
            />
          </div>
          <div className="flex items-center mt-5">
            <Textarea
              placeholder="Type your message here."
              value={message}
              onChange={(e) => setMessage(e.target.value)}
              onKeyDown={(e) => {
                if (e.key === 'Enter' && !e.shiftKey) {
                  e.preventDefault(); // Prevents the default action (new line)
                  handleSendMessage();
                }
              }}
              className="flex-grow" // Ensures textarea takes available space
            />
            <PaperPlaneIcon
              onClick={handleSendMessage}
              className="text-blue-700 scale-150 cursor-pointer hover:scale-[2.0] ml-5"
            />
          </div>
        </div>
        {/* end message */}
        <div className="w-2/5 border-l-2 px-5">
          <div className="flex flex-col">
            <div className="font-semibold text-xl py-4">Mern Stack Group</div>
            <img
              src="https://source.unsplash.com/L2cxSuKWbpo/600x600"
              className="object-cover rounded-xl h-64"
              alt=""
            />
            <div className="font-semibold py-4">Created 22 Sep 2021</div>
            <div className="font-light">
              Lorem ipsum dolor sit amet consectetur adipisicing elit. Deserunt,
              perspiciatis!
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Chat;
