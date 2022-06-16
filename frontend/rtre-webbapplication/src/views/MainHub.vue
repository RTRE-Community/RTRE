<template>
<v-container class="mt-16">
    <v-layout row>
        <v-flex xs12 md6>
            <v-container class="mb-12">
                <FunctionHub></FunctionHub>
            </v-container>
        </v-flex>
        <v-flex xs12 md6>
            <div v-if="!overlay">
            <v-container>
                <SearchBox></SearchBox>
            </v-container>
            </div>
        </v-flex>
            <v-container>
               <ChatWIndow />
            </v-container>
    </v-layout>
</v-container>
</template>

<script>
import SearchBox from "@/components/SearchBox.vue";
import FunctionHub from "@/components/FunctionHub.vue";
import ChatWIndow from '../components/ChatWIndow.vue'
const EventEmitter = require('../EventEmitter')

export default ({
    name: 'MainHub',
    components: {
        SearchBox,
        FunctionHub,
        ChatWIndow
    },
    created(){
        EventEmitter.eventEmitter.on('disableProjectBox', this.disableProjectBox);
        EventEmitter.eventEmitter.on('enableProjectBox', this.enableProjectBox);

    },
    data() {
    return {
        overlay: false,
      
      } // specifies the color scheme for the component
     
    },
  methods: {
    disableProjectBox(){
        this.overlay = true;
    },
    enableProjectBox(){
        this.overlay = false;
    }
    }
})
</script>

<style>

</style>
