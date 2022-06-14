<template>
<div>
    <v-container>
        <v-layout row>
            <v-flex xs12 md12>
                <v-container>
                    <ModelViewer></ModelViewer>
                </v-container>
            </v-flex>
        </v-layout>
    </v-container>
</div>
</template>

<script>
import ModelViewer from "@/components/ModelViewer.vue";

export default ({
    name: 'BimViewer',
    components: {
        ModelViewer
    }
})
</script>
