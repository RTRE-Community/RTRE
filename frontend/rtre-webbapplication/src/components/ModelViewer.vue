<template>
<v-app class="pb10">
    <input type="file" name="load" id="file-input" />
    <v-card id="wrapper" style="width: 100%;height: 55vh;" elevation="3" class="mt-5">
        <canvas id="three-canvas"></canvas>
    </v-card>
    <v-card-text>
        <v-slider v-model="projectTickValue" :tick-labels="ticksLabels" :min="0" :max="ticksLabels.length - 1" step="1" ticks="always" tick-size="5"></v-slider>
    </v-card-text>

    <v-overlay :value="overlay[0]">
        <v-progress-circular indeterminate size="64"></v-progress-circular>
    </v-overlay>
</v-app>
</template>

<script>
import {
    AmbientLight,
    AxesHelper,
    DirectionalLight,
    GridHelper,
    PerspectiveCamera,
    Scene,
    WebGLRenderer,
} from "three";
import {
    OrbitControls
} from "three/examples/jsm/controls/OrbitControls";
import {
    IFCLoader
} from "web-ifc-three/IFCLoader";
import axios from "axios";
import Vue from "vue";
export default {
    name: "ModelViewer",
    data() {
        return {
            LatestIfcModel: null,
            scene: null,
            project: null,
            projectTickValue: null,
            ticksLabels: [],
            dateMapOfSubProjects: null,
            overlay: [true],
            initSceneChildElements: []
        }
    },
    mounted() {
        // get oid
        let param = this.$route.params.oid

        // first render and populate timeline if requested
        if (param != ':oid') {
            axios.get(process.env.VUE_APP_RTRE_BACKEND_PORT + "/api/getDateAndSubProject?token=" + sessionStorage.getItem('TokenId') +
                    "&id=" + param)
                .then((resp) => {
                    this.ticksLabels = resp.data
                }).then(() => {
                    for (let i = 0; i < this.ticksLabels.length; i++) {
                        if (this.ticksLabels[i] == param) {
                            this.projectTickValue = i
                        }
                    }

                    setTimeout(() => {
                        Vue.set(this.overlay, 0, false);
                    }, 1000);
                })

        } else {
            setTimeout(() => {
                Vue.set(this.overlay, 0, false);
            }, 1000);
        }

        //render scene such as shades, colour and grid
        this.renderScene()

        //keep count of child elements used for deletWithOid()
        Vue.set(this.initSceneChildElements, 0, this.scene.children.length)
    },
    watch: {
        projectTickValue(newValue, oldValue) {
            Vue.set(this.overlay, 0, true);
            if (oldValue == null) {
                return
            } else {
                axios({
                    url: process.env.VUE_APP_RTRE_BACKEND_PORT + "/api/getIfc?fileName=" + this.ticksLabels[newValue],
                    methods: "GET",
                }).then((res) => {

                    var render = URL.createObjectURL(new Blob([res.data], {
                        type: "application/octet-stream"

                    }))
                    this.renderModel(render)
                }).then(() => {
                    this.deleteWithOid()
                    console.log("deleting...")
                    setTimeout(() => {
                        Vue.set(this.overlay, 0, false);
                    }, 1000);
                })

            }
        },
    },
    methods: {
        renderModel(ifcURL) {

            const ifcLoader = new IFCLoader();
            ifcLoader.ifcManager.setWasmPath("../");

            ifcLoader.load(
                ifcURL,
                (ifcModel) => this.scene.add(ifcModel));
            //Creates the Three.js this.scene

        },
        deleteWithOid() {
            // initial render will create 5 child elements if its 4 and delete is being called dont remove anything (because user generated model has already been removed)
            if ((this.initSceneChildElements[0] - 1) !== this.scene.children.length) {
                this.scene.remove(this.scene.children[this.scene.children.length - 1]);
            }
        },
        renderScene() {
            let vCardWrapper = document.getElementById('wrapper');
            let vCardWrapperDimensions = vCardWrapper.getBoundingClientRect();
            //Creates the Three.js this.scene
            this.scene = new Scene();
            //Object to store the size of the viewport
            const size = {
                width: vCardWrapperDimensions.width,
                height: vCardWrapperDimensions.height
            };

            //Creates the camera (point of view of the user)
            const aspect = size.width / size.height;
            const camera = new PerspectiveCamera(75, aspect, 0.1, 1000);
            camera.position.z = 15;
            camera.position.y = 13;
            camera.position.x = 8;

            //Creates the lights of the this.scene
            const lightColor = 0xffffff;

            const ambientLight = new AmbientLight(lightColor, 0.5);
            this.scene.add(ambientLight);

            const directionalLight = new DirectionalLight(lightColor, 1);
            directionalLight.position.set(0, 10, 0);
            directionalLight.target.position.set(-5, 0, 0);
            this.scene.add(directionalLight);
            this.scene.add(directionalLight.target);

            //Sets up the renderer, fetching the canvas of the HTML
            const threeCanvas = document.getElementById("three-canvas");
            const renderer = new WebGLRenderer({
                canvas: threeCanvas,
                alpha: true
            });

            renderer.setSize(size.width, size.height);
            renderer.setPixelRatio(Math.min(window.devicePixelRatio, 2));

            //Creates grids and axes in the this.scene
            const grid = new GridHelper(75, 75);
            this.scene.add(grid);

            const axes = new AxesHelper();
            axes.material.depthTest = false;
            axes.renderOrder = 1;
            this.scene.add(axes);

            //Creates the orbit controls (to navigate the this.scene)
            const controls = new OrbitControls(camera, threeCanvas);
            controls.enableDamping = true;
            controls.target.set(-2, 0, 0);

            //Animation loop
            const animate = () => {
                controls.update();
                renderer.render(this.scene, camera);
                requestAnimationFrame(animate);
            };

            animate();

            //Adjust the viewport to the size of the browser
            window.addEventListener("resize", () => {
                let vCardWrapper = document.getElementById('wrapper');
                let vCardWrapperDimensions = vCardWrapper.getBoundingClientRect();
                size.width = window.innerWidth;
                size.height = window.innerHeight;
                camera.aspect = vCardWrapperDimensions.width / vCardWrapperDimensions.height;
                camera.updateProjectionMatrix();
                renderer.setSize(vCardWrapperDimensions.width, vCardWrapperDimensions.height);
            });
            const ifcLoader = new IFCLoader();
            ifcLoader.ifcManager.setWasmPath("../");

            const input = document.getElementById("file-input");
            input.addEventListener(
                "change",
                (changed) => {
                    this.deleteWithOid()
                    console.log("deleting......")
                    const file = changed.target.files[0];
                    var ifcURL = URL.createObjectURL(file);
                    this.renderModel(ifcURL, this.scene)
                },
                false
            );

            // var paramsOid = this.$route.params.oid
            // if (paramsOid != ':oid') {
            //     axios({
            //         url: process.env.VUE_APP_RTRE_BACKEND_PORT + "/api/getIfc?fileName=" + paramsOid,
            //         methods: "GET",
            //     }).then((res) => {
            //         Vue.set(this.overlay, 0, false);
            //         var render = URL.createObjectURL(new Blob([res.data], {
            //             type: "application/octet-stream"
            //         }))
            //         this.renderModel(render)
            //     })
            // } else {
            Vue.set(this.overlay, 0, false)
            // }
        }
    }
}
</script>
