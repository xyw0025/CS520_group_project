'use client';

import { useState, useEffect } from 'react';
import { useUserService } from '@/utils';
import ChattingRoomUser from '@/components/ChattingRoomUser';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';

const Chat = () => {
  const userService = useUserService();
  const currentUser = userService.currentUser;
  const [matchedUsers, setMatchedUsers] = useState(userService.matchedUsers);

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
      senderId: '656f8e446df5997883c67bf3',
      receiverId: '656f8f076df5997883c67bf5',
      messageText: message,
    };

    stompClient.publish({
      destination: '/app/sendMessage', // The destination endpoint
      body: JSON.stringify(chatMessage), // Message body
    });
  }

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

  //   return (
  //     <div className="m-20">
  //       <label htmlFor="" className="font-bold">
  //         input
  //       </label>
  //       <input
  //         type="text"
  //         value={message}
  //         onChange={(e) => setMessage(e.target.value)}
  //       />
  //       <button className="bg-blue-400" onClick={handleSendMessage}>
  //         Send
  //       </button>
  //     </div>
  //   );
  // };
  return (
    <div className="container mx-auto shadow-lg rounded-lg">
      {/* Chatting */}
      <div className="flex flex-row justify-between bg-white">
        {/* chat list */}
        <div className="flex flex-col w-2/5 border-r-2 overflow-y-auto">
          {/* user list */}
          {matchedUsers &&
            matchedUsers.map((matchedUser) => (
              <ChattingRoomUser user={matchedUser} />
            ))}
          {/* end user list */}
        </div>
        {/* end chat list */}
        {/* message */}
        <div className="w-full px-5 flex flex-col justify-between">
          <div className="flex flex-col mt-5">
            <div className="flex justify-end mb-4">
              <div className="mr-2 py-3 px-4 bg-blue-400 rounded-bl-3xl rounded-tl-3xl rounded-tr-xl text-white">
                Welcome to group everyone !
              </div>
              <img
                src="https://source.unsplash.com/vpOeXr5wmR4/600x600"
                className="object-cover h-8 w-8 rounded-full"
                alt=""
              />
            </div>
            <div className="flex justify-start mb-4">
              <img
                src="https://source.unsplash.com/vpOeXr5wmR4/600x600"
                className="object-cover h-8 w-8 rounded-full"
                alt=""
              />
              <div className="ml-2 py-3 px-4 bg-gray-400 rounded-br-3xl rounded-tr-3xl rounded-tl-xl text-white">
                Lorem ipsum dolor sit amet consectetur adipisicing elit. Quaerat
                at praesentium, aut ullam delectus odio error sit rem.
                Architecto nulla doloribus laborum illo rem enim dolor odio
                saepe, consequatur quas?
              </div>
            </div>
            <div className="flex justify-end mb-4">
              <div>
                <div className="mr-2 py-3 px-4 bg-blue-400 rounded-bl-3xl rounded-tl-3xl rounded-tr-xl text-white">
                  Lorem ipsum dolor, sit amet consectetur adipisicing elit.
                  Magnam, repudiandae.
                </div>

                <div className="mt-4 mr-2 py-3 px-4 bg-blue-400 rounded-bl-3xl rounded-tl-3xl rounded-tr-xl text-white">
                  Lorem ipsum dolor sit amet consectetur adipisicing elit.
                  Debitis, reiciendis!
                </div>
              </div>
              <img
                src="https://source.unsplash.com/vpOeXr5wmR4/600x600"
                className="object-cover h-8 w-8 rounded-full"
                alt=""
              />
            </div>
            <div className="flex justify-start mb-4">
              <img
                src="https://source.unsplash.com/vpOeXr5wmR4/600x600"
                className="object-cover h-8 w-8 rounded-full"
                alt=""
              />
              <div className="ml-2 py-3 px-4 bg-gray-400 rounded-br-3xl rounded-tr-3xl rounded-tl-xl text-white">
                happy holiday guys!
              </div>
            </div>
          </div>
          <div className="py-5">
            <input
              className="w-full bg-gray-300 py-5 px-3 rounded-xl"
              type="text"
              placeholder="type your message here..."
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
