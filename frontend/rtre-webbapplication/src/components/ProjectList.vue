<template>
  <div id="app">
    <v-app id="inspire">
      <v-expansion-panels>
        <template v-for="project in projectList">
          <v-expansion-panel v-if="project.parentId == -1" :key="project.id">
            <v-expansion-panel-header>
              {{ project.name }}
            </v-expansion-panel-header>
            <v-expansion-panel-content>
              <li>id:{{ project.oid }}</li>
              <li>Date: {{ project.createdDate }}</li>
              <li>Description: {{ project.description }}</li>
              <li v-if="project.subProjects.length != 0">
                {{ project.subProjects }}
              </li>
              <v-expansion-panels>
                <template v-for="subProjects in projectList">
                  <v-expansion-panel
                    class="mt-6"
                    v-if="subProjects.parentId == project.oid"
                    :key="subProjects.id"
                  >
                    <v-expansion-panel-header>
                      {{ subProjects.name }}</v-expansion-panel-header
                    >
                    <v-expansion-panel-content
                      >¨
                      <li>id: {{ subProjects.oid }}</li>
                      <li>parentId : {{ subProjects.parentId }}</li>
                    </v-expansion-panel-content>
                  </v-expansion-panel>
                </template>
              </v-expansion-panels>
            </v-expansion-panel-content>
          </v-expansion-panel>
        </template>
      </v-expansion-panels>
    </v-app>
  </div>
</template>

<script>
import axios from "axios";
export default {
  name: "ProjectList",
  data() {
    return {
      projectList: [],
    };
  },
  mounted() {
    axios.get("http://localhost:3030/api/getProjectList").then((resp) => {
      this.projectList = resp.data;
      console.log(this.projectList);
    });
  },
};
</script>

<style></style>
