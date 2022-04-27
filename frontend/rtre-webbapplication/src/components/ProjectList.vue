<template>
<div id="app">
  <v-app id="inspire">
    <v-expansion-panels>
      <v-expansion-panel v-for="project in projectList" :key="project.id">
        <v-expansion-panel-header>
        {{project.name}}
        </v-expansion-panel-header>
        <v-expansion-panel-content>
          <li>{{project.createdById}}</li>
          <li>{{project.createdDate}}</li>
          <li>{{project.description}}</li>
          <li v-if="project.subProjects.length !=0">{{project.subProjects}}</li>
        </v-expansion-panel-content>
      </v-expansion-panel>
    </v-expansion-panels>
  </v-app>
</div>
</template>

<script>
import axios from 'axios'
export default {
name:"ProjectList",
data(){
  return{
    projectList:[],
  }
},
mounted(){
  axios
  .get('http://localhost:3030/api/getProjectList')
    .then((resp) => {this.projectList = resp.data;
    console.log(this.projectList)})
}
}
</script>

<style>

</style>