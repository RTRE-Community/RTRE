<template>
<v-container class="mt-16">
    <v-layout row>
        <v-flex xs12 md6>
            <v-container class="mb-12">
                <FunctionHub></FunctionHub>
            </v-container>
        </v-flex>
        <v-flex xs12 md6>
            <v-container>
                <SearchBox :key="componentKey"></SearchBox>
            </v-container>
        </v-flex>
        <v-btn @click="forceRerender()">Refresh</v-btn>
    </v-layout>
</v-container>
</template>

<script>
import SearchBox from "@/components/SearchBox.vue";
import FunctionHub from "@/components/FunctionHub.vue";

export default ({
    name: 'MainHub',
    data(){
        return{
            componentKey: 0,
        }
    },
    methods:{
      forceRerender(){
        this.componentKey += 1;
      }  
    },
    components: {
        SearchBox,
        FunctionHub
    }
})
</script>
