'use client';

import { useState, useEffect, useRef } from 'react';
import { IUser, Message } from '@/utils';
import { Textarea } from '@/components/ui/textarea';
import { useUserService } from '@/utils';
import { PaperPlaneIcon } from '@radix-ui/react-icons';
import ChattingRoomUser from '@/components/ChattingRoomUser';
import MessageBox from '@/components/MessageBox';
import ChattingRoomProfile from '@/components/ChattingRoomProfile';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';

// Chatting Room Process
// step 1. fetch matchedUsers
// step 2. choose a user -> set currentChatUser -> fetch our conversation_id and conversation
// step 3. stompClient connect to backend url
// step 4. use conversation_id to send message and listen message in private room
const Chat = () => {
  const userService = useUserService();
  const currentUser = userService.currentUser;
  const [matchedUsers, setMatchedUsers] = useState(userService.matchedUsers);
  const [currentChatUser, setCurrentChatUser] = useState<IUser | null>(null);
  const [conversationId, setConversationId] = useState<string | null>(null);
  const [conversationMessages, setConversationMessages] = useState<Message[]>(
    []
  );
  const [message, setMessage] = useState('');

  //IMEComposing
  const [isIMEComposing, setIsIMEComposing] = useState(false);
  const handleCompositionStart = () => {
    setIsIMEComposing(true);
  };

  const handleCompositionEnd = () => {
    setIsIMEComposing(false);
  };

  //Handle new message scroll to bottom
  const messagesEndRef = useRef<HTMLDivElement>(null);
  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  };

  useEffect(() => {
    scrollToBottom();
  }, [conversationMessages]);

  useEffect(() => {
    scrollToBottom();
  }, [conversationMessages]);

  // Fetch conversation ID and messages
  useEffect(() => {
    const fetchConversationData = async () => {
      if (currentUser && currentChatUser) {
        try {
          // Fetch conversation ID
          const fetchedConversationId = await userService.getConversationId(
            currentUser.id,
            currentChatUser.id
          );
          setConversationId(fetchedConversationId);

          // Fetch conversation messages
          const response = await userService.getConversation(
            currentUser.id,
            currentChatUser.id
          );
          setConversationMessages(response);
        } catch (error) {
          console.error('Error fetching conversation data:', error);
        }
      }
    };

    fetchConversationData();
  }, [currentChatUser, currentUser]);

  // Fetch matched users
  useEffect(() => {
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

  // Connect to the WebSocket server
  const API_URL = process.env.NEXT_PUBLIC_API_URL;
  const socket = new SockJS(`${API_URL}/chat`);
  const stompClient = new Client({
    webSocketFactory: () => socket,
  });
  stompClient.onConnect = () => {};
  stompClient.activate();

  const handleUserSelect = (user: IUser) => {
    setCurrentChatUser(user);
  };

  const handleSendMessage = () => {
    if (!conversationId) {
      console.error('Conversation ID not set. Cannot send message.');
      return;
    }

    const chatMessage = {
      senderId: currentUser?.id,
      receiverId: currentChatUser?.id,
      messageText: message,
      createdAt: new Date().toISOString(), // Assuming createdAt is a date string
    };

    // Send the message to the specific conversation channel
    stompClient.publish({
      destination: `/app/room/${conversationId}/sendMessage`,
      body: JSON.stringify(chatMessage),
    });

    setMessage(''); // Clear the input field
  };

  // Subscribe to a room to receive messages by building a WebSocket
  useEffect(() => {
    let isSubscribed = true;

    const stompClient = new Client({
      webSocketFactory: () => new SockJS(`${API_URL}/chat`),
      onConnect: () => {
        console.log('WebSocket connected!');

        // Subscribe to the conversation channel if conversationId is available
        if (conversationId && isSubscribed) {
          stompClient.subscribe(`/room/${conversationId}`, (message) => {
            const receivedMessage = JSON.parse(message.body);
            setConversationMessages((prevMessages) => [
              ...prevMessages,
              receivedMessage,
            ]);
          });
        }
      },
    });

    stompClient.activate();

    return () => {
      stompClient.deactivate();
      isSubscribed = false;
    };
  }, [conversationId]);

  return (
    <div className="container mx-auto shadow-lg rounded-lg my-1 h-5/6">
      {/* Chatting */}
      <div className="flex flex-row justify-between bg-white h-full">
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
        <div className="w-full px-5 flex flex-col  justify-between">
          {/* Messages display */}
          <div className="flex flex-col mt-5 flex-grow overflow-y-auto">
            {conversationMessages.map((msg) => (
              <MessageBox
                text={msg.messageText}
                isSender={msg.senderId === currentUser?.id}
                imageUrl={
                  msg.senderId === currentUser?.id
                    ? currentUser?.profile?.imageUrls?.[0] || 'defaultImageUrl'
                    : currentChatUser?.profile?.imageUrls?.[0] ||
                      'defaultImageUrl'
                }
                createdAt={msg.createdAt}
              />
            ))}
            <div ref={messagesEndRef} />
          </div>
          {currentChatUser && (
            <div className="flex items-center mt-2 p-3 bg-white border-t-2">
              <Textarea
                placeholder="Type your message here."
                value={message}
                onChange={(e) => setMessage(e.target.value)}
                onKeyDown={(e) => {
                  if (
                    e.key === 'Enter' &&
                    !e.shiftKey &&
                    message.trim() !== '' &&
                    !isIMEComposing
                  ) {
                    e.preventDefault(); // Prevents the default action (new line)
                    handleSendMessage();
                  }
                }}
                onCompositionStart={handleCompositionStart}
                onCompositionEnd={handleCompositionEnd}
                className="flex-grow" // Ensures textarea takes available space
              />
              <PaperPlaneIcon
                onClick={handleSendMessage}
                className="text-blue-700 scale-150 cursor-pointer hover:scale-[2.0] ml-5"
              />
            </div>
          )}
        </div>
        {/* end message */}
        <div className="w-2/5 border-l-2 px-1">
          {currentChatUser && <ChattingRoomProfile user={currentChatUser} />}
        </div>
      </div>
    </div>
  );
};

export default Chat;
