<template>
<div class="ma-4">
  <v-container class="ma-5"><FunctionHub /></v-container>
  <v-container class="ma-5 "><ProjectList /></v-container>
  
   

</div>

</template>

<script>
import ProjectList from './components/ProjectList.vue';
import FunctionHub from './components/FunctionHub.vue';

export default {
  name: 'App',

  components: {
    ProjectList,
    FunctionHub
  },

  data: () => ({
    //
  }),
};
</script>
