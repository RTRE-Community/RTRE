<template>
<v-container class="mt-16">
      <div v-if="this.showOverlay==true && this.Users !== null">
          <v-overlay
          :value="overlay"
        >
        <v-btn color="warning"
             :value="overlay"
             @click="this.closeOverlay">Cancel</v-btn>  
        <h1>Select User to Chat with</h1><br>
    <v-list >
      <v-subheader>USERS</v-subheader>
      <v-list-item-group v-model="Users"
       color="primary">
        <v-list-item 
          v-for="User in Users"
          :key="User.oid"
          @click="loadUser(User)"
        >
          <v-list-item-icon>
            <v-icon v-text="User.icon"></v-icon>
          </v-list-item-icon>
          <v-list-item-content>
            <v-list-item-title v-text="User.name"></v-list-item-title>
            <div v-if="getCount(User.oid) > 0">
              <v-list-item-subtitle>New Messages: {{ getCount(User.oid) }}</v-list-item-subtitle>
            </div> 
          </v-list-item-content>
        </v-list-item>
      </v-list-item-group>
    </v-list>
         </v-overlay>
      </div>
            <v-container>
               <beautiful-chat v-model="jsonFile"
      :participants="participants"
      :titleImageUrl="titleImageUrl"
      :onMessageWasSent="onMessageWasSent"
      :messageList="messageList"
      :newMessagesCount="newMessagesCount"
      :isOpen="isChatOpen"
      :close="closeChat"
      :icons="icons"
      :open="openChat"
      :showEmoji="true"
      :showFile="true"
      :showEdition="true"
      :showDeletion="true"
      :showTypingIndicator="showTypingIndicator"
      :showLauncher="true"
      :showCloseButton="true"
      :colors="colors"
      :alwaysScrollToBottom="alwaysScrollToBottom"
      :messageStyling="messageStyling"
      @onType="handleOnType"
      @edit="editMessage" />
            </v-container>
</v-container>

</template>

<script>
import CloseIcon from '../assets/close-icon.png'
import OpenIcon from '../assets/logo-no-bg.svg'
import FileIcon from '../assets/file.svg'
import CloseIconSvg from '../assets/close.svg'
const EventEmitter = require('../EventEmitter')
import axios from 'axios'

export default ({
    name: 'ChatWindow',
    props: ['overlay', 'allUsers', 'userMessages'],
    components: {
    },
    created() {
    this.interval = setInterval(() => this.updateMessages(true), 3000);
    },
    mounted(){
      this.Users = JSON.parse(localStorage.getItem("Users") || "[]")

      axios.get(process.env.VUE_APP_RTRE_BACKEND_PORT + '/api/getUserMessages?' + new URLSearchParams({
                token: sessionStorage.getItem("TokenId"),
                username: sessionStorage.getItem('Username')
            })).then((resp) => {
                this.UserMessages = resp.data
                this.updateMessages(false);
                console.log(resp.data);

            });

      for(let i = 0; i < this.Users.length; i++){
        if(this.Users[i].oid == sessionStorage.getItem('oid')){
          this.Users.splice(i, 1);
          }
      }
      for(let i = 0; i < this.Users.length; i++){
              this.Users[i].messages = {};
              this.Users[i].messages.messageCount = 0;
              this.Users[i].messages['message'] = [];
          }
    
    },
    data() {
    return {
      lastMessage: {},
      jsonFile: null,
      parsedJsonFile: {},
      response: '',
      openedChatOnce: false,
      UserMessages: [],
      user: {},
      showChatBox: false,
      showOverlay: false,
      Users: [],
      selectedUser: {},
      allMessages: [],
      icons:{
        open:{
          img: OpenIcon,
          name: 'default',
        },
        close:{
          img: CloseIcon,
          name: 'default',
        },
        file:{
          img: FileIcon,
          name: 'default',
        },
        closeSvg:{
          img: CloseIconSvg,
          name: 'default',
        },
      },
      participants: [], // the list of all the participant of the conversation. `name` is the user name, `id` is used to establish the author of a message, `imageUrl` is supposed to be the user avatar.
      titleImageUrl: 'https://a.slack-edge.com/66f9/img/avatars-teams/ava_0001-34.png',
      messageList: [
        
      ], // the list of the messages to show, can be paginated and adjusted dynamically
      newMessagesCount: 0,
      isChatOpen: false, // to determine whether the chat window should be open or closed
      showTypingIndicator: '', // when set to a value matching the participant.id it shows the typing indicator for the specific user
      colors: {
        header: {
          bg: '#4e8cff',
          text: '#ffffff'
        },
        launcher: {
          bg: '#4e8cff'
        },
        messageList: {
          bg: '#ffffff'
        },
        sentMessage: {
          bg: '#4e8cff',
          text: '#ffffff'
        },
        receivedMessage: {
          bg: '#eaeaea',
          text: '#222222'
        },
        userInput: {
          bg: '#f4f7f9',
          text: '#565867'
        }
      }, // specifies the color scheme for the component
      alwaysScrollToBottom: false, // when set to true always scrolls the chat to the bottom when new events are in (new message, user starts typing...)
      messageStyling: true // enables *bold* /emph/ _underline_ and such (more info at github.com/mattezza/msgdown)
    }
  },
  methods: {
    closeOverlay(){
      this.isChatOpen = !this.isChatOpen;
      EventEmitter.eventEmitter.emit('enableProjectBox', '');
      this.showOverlay = false;
    },
    updateMessages(bool){
      if(!bool){

           for(let i = 0; i < this.Users.length; i++){

                    for(let j = 0; j < this.UserMessages.length; j++){
                      
                      if(parseInt(this.UserMessages[j].from) === this.Users[i].oid){
                        
                        let m = {'message': this.UserMessages[j].message};
                        this.Users[i].messages.message.push(m);

                        let message = { type: 'text', author: this.UserMessages[j].from, data: { text: this.UserMessages[j].message } }
                
                        const containsObject = function(obj, list) {
                          let i;
                          for (i = 0; i < list.length; i++) {
                            if(JSON.stringify(list[i]) === JSON.stringify(obj)) {
                            
                            return true;
                          }
                        }
                        return false;
                        }
                        if(containsObject(message, this.messageList) === false){
                          this.Users[i].messages.messageCount++;
                          this.newMessagesCount++;
                          this.messageList = [ ...this.messageList, message ];
                          this.lastMessage = message;
                          console.log('new message loaded');
                        }
                      }
                    }          
                  }
                  this.UserMessages = null;
                  return;

      }
      axios.get(process.env.VUE_APP_RTRE_BACKEND_PORT + '/api/getUserMessages?' + new URLSearchParams({
                token: sessionStorage.getItem("TokenId"),
                username: sessionStorage.getItem('Username')
            })).then((resp) => {
                this.openedChatOnce = false;
                console.log('updating');
                if(resp.data.length > 0){
                  this.UserMessages = resp.data;
                  for(let i = 0; i < this.Users.length; i++){

                    for(let j = 0; j < this.UserMessages.length; j++){
                      
                      if(parseInt(this.UserMessages[j].from) === this.Users[i].oid){
                        console.log(parseInt(this.UserMessages[j].from));
                        console.log(this.Users[i].oid);
                        let m = {'message': this.UserMessages[j].message};
                        this.Users[i].messages.message.push(m);

                        let message = { type: 'text', author: this.UserMessages[j].from, data: { text: this.UserMessages[j].message } }
                
                        const containsObject = function(obj, list) {
                          let i;
                          for (i = 0; i < list.length; i++) {
                            if(JSON.stringify(list[i]) === JSON.stringify(obj)) {
                            
                            return true;
                          }
                        }
                        return false;
                        }
                        if(containsObject(message, this.messageList) === false){
                          this.Users[i].messages.messageCount++;
                          this.newMessagesCount++;
                          this.messageList = [ ...this.messageList, message ];
                          this.lastMessage = message;
                          console.log('new message loaded');
                        }
                      }else {
                        console.log(this.UserMessages);
                        console.log(parseInt(this.UserMessages[j].from));
                        console.log(this.Users[i].oid);
                      }
                    }          
                  }
                  this.UserMessages = null;
                }
            });
    },
    getCount(id){
      
      const index = this.Users.map(e => e.oid).indexOf(id);
      let count = this.Users[index].messages.messageCount;
      return count;
      
    },
    loadUser(User){

      this.user = User;
      const p = [{
        name: User.name,
        id: User.oid,
        imageUrl: null
      }];
      const index = this.Users.map(e => e.oid).indexOf(User.oid);
      this.Users[index].messages.messageCount = 0;
      this.participants = p;
      console.log(this.Users);
      this.showOverlay = false;
      this.showChatBox = true;
      localStorage.setItem("Users", JSON.stringify(this.Users))
      axios.get(process.env.VUE_APP_RTRE_BACKEND_PORT + '/api/readMessages?' + new URLSearchParams({
                token: sessionStorage.getItem('TokenId'),
                username: sessionStorage.getItem('Username'),
                sender: this.user.oid
            })).then((resp) => {
              if(resp.status == 200){
                console.log(200);
              }
            

            });
    },
    sendMessage (text) {
      if (text.length > 0) {
        this.newMessagesCount = this.isChatOpen ? this.newMessagesCount : this.newMessagesCount + 1

        this.newMessagesCount = this.isChatOpen ? this.newMessagesCount : this.newMessagesCount + 1
        let today = new Date();
        let date = today.getFullYear()+'-'+(today.getMonth()+1)+'-'+today.getDate();
        let time = today.getHours() + ":" + today.getMinutes() + ":" + today.getSeconds();
        let dateTime = date+' '+time;
        let Message = {};
        console.log(text);
        console.log(JSON.stringify(text));
        console.log(JSON.parse(text));
        Message.author = sessionStorage.getItem('Username');
        if(typeof text === 'string' || text instanceof String){
          Message.type = 'text';
          Message.data = text;
          console.log()
        } else if (text === undefined){
          Message.type = 'json';
          Message.data = JSON.parse(text);
        }
       
        Message.date = dateTime;
        Message.to =  this.user.oid;

        this.onMessageWasSent(Message)
      }
    },
    async onMessageWasSent (message) {
      // called when the user sends a message
        let today = new Date();
        let date = today.getFullYear()+'-'+(today.getMonth()+1)+'-'+today.getDate();
        let time = today.getHours() + ":" + today.getMinutes() + ":" + today.getSeconds();
        let dateTime = date+' '+time;
        let Message = {};
        Message.author = sessionStorage.getItem('TokenId');
        Message.from = sessionStorage.getItem('oid');
        let textMessage = null;
        if(typeof message.data.text === 'string' || message.data.text instanceof String){
          Message.type = 'text';
          Message.data = message.data.text;
          textMessage = Message.data;
        } else if(message.data.file.name.includes('.json')) {
          Message.type = 'json';
          this.jsonFile = message.data.file;
          const fileToJSON = async function fileToJSON(file) {
            return new Promise((resolve, reject) => {
            const fileReader = new FileReader();
            fileReader.onload = (event) => resolve(JSON.parse(event.target.result));
            fileReader.onerror = error => reject(error);
            fileReader.readAsText(file);
            })
          }
        this.parsedJsonFile = await fileToJSON(this.jsonFile);
    
        Message.data = this.parsedJsonFile;
        console.log(Message);
        } else {
          console.log('error reading user input');
        }
        Message.date = dateTime;
        Message.to = this.user.oid;
        let text =  { type: 'text', author: 'me', data: { text: textMessage } }
        this.messageList = [ ...this.messageList, text ];
        axios.get(process.env.VUE_APP_RTRE_BACKEND_PORT + '/api/sendMessage?' + new URLSearchParams({
                token: sessionStorage.getItem('TokenId'),
                message: Message.data,
                from: sessionStorage.getItem('oid'),
                to: Message.to,
                date: Message.date 
            })).then((resp) => {
                console.log(resp.status);
            });

    },
    openChat () {
    
      this.isChatOpen = !this.isChatOpen;
      this.newMessagesCount = 0;
      EventEmitter.eventEmitter.emit('disableProjectBox', this.u);
      this.showOverlay = true;

    },
    closeChat () {
      // called when the user clicks on the botton to close the chat
      this.isChatOpen = !this.isChatOpen;
      EventEmitter.eventEmitter.emit('enableProjectBox', '');
      this.showOverlay = false;
      this.messageList = [];
      this.participants = [];
      this.Users = JSON.parse(localStorage.getItem("Users") || "[]")
      setInterval(this.updateMessages(true), 3000);

    },
    handleScrollToTop () {
      // called when the user scrolls message list to top
      // leverage pagination for loading another page of messages
    },
    handleOnType () {
      //console.log('Emit typing event')
    },
    editMessage(message){
      const m = this.messageList.find(m=>m.id === message.id);
      m.isEdited = true;
      m.data.text = message.data.text;
    }
    }
})
</script>

<style>

</style>
