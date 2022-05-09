<template>
  <v-app class="ma-10">
    <v-container class="mt-16">
      <v-layout row>
        <v-flex xs12 md6>
          <v-container class="mb-12"><FunctionHub /></v-container>  
        </v-flex>
        <v-flex xs12 md6>
          <v-container><ProjectList /></v-container>
        </v-flex>
      </v-layout>
    </v-container>
  </v-app>
</template>

<script>
import ProjectList from "./components/ProjectList.vue";
import FunctionHub from "./components/FunctionHub.vue";
export default {
  name: "App",

  components: {
    ProjectList,
    FunctionHub
  },

  data: () => ({
    //
  }),
};
</script>
