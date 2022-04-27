<template>
  <v-card>
    <v-toolbar
      flat
      color="primary"
      dark
    >
      <v-toolbar-title>User Profile</v-toolbar-title>
    </v-toolbar>
    <v-tabs vertical>
      <v-tab>
        <v-icon left>
          mdi-archive-arrow-up
        </v-icon>
        Check in
      </v-tab>
      <v-tab>
        <v-icon left>
          mdi-cloud-download
        </v-icon>
        Check Out
      </v-tab>
      <v-tab>
        <v-icon left>
          mdi-call-merge
        </v-icon>
        Merge
      </v-tab>

      <v-tab-item>
        <v-card flat>
          <v-card-text>
          <FileDrop/>
          </v-card-text>
        </v-card>
      </v-tab-item>
      <v-tab-item>
        <v-card flat>
          <v-card-text>
         <form action="/action_page.php">
  <label for="id">First name:</label><br>
  <input type="text" id="id" name="id" value="Input Id of desired project!"><br>
  <input type="submit" value="Submit">
</form> 
          </v-card-text>
        </v-card>
      </v-tab-item>
      <v-tab-item>
        <v-card flat>
          <v-card-text>
             <h1> first merge file:</h1>
             <FileDrop/>
             <h1> Second merge file:</h1>
             <FileDrop/>
              <h1> Output file:</h1>
              <FileDrop/>
          </v-card-text>
        </v-card>
      </v-tab-item>
    </v-tabs>
  </v-card>
</template>
<script>
import FileDrop from './FileDrop.vue'
export default {
  components: { FileDrop },
name:"FunctionHub"
}
</script>

<style>

</style>