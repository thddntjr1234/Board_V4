import {createWebHistory, createRouter} from "vue-router";
import ErrorPage from "@/views/ErrorView.vue";
import CommunityBoardView from "@/views/CommunityBoardView.vue";
import CommunityView from "@/views/CommunityView.vue";
import CommunityWriteFormView from "@/views/CommunityWriteFormView.vue";
import MainPage from "@/views/MainPage.vue";
import CommunityModifyView from "@/views/CommunityModifyView.vue";


const router = createRouter({
    history: createWebHistory(),
    routes: [ // path별 component를 추가한다.
        {
            path: "/",
            name: "MainPage",
            component: MainPage
        },
        {
            path: "/:pathMatch(.*)",
            name: "not-found",
            component: ErrorPage
        },
        {
            path: "/boards/free",
            name: "CommunityBoardView",
            component: CommunityBoardView
        },
        {
            path: "/boards/free/new",
            name: "CommunityWriteFormView",
            component: CommunityWriteFormView
        },
        {
            path: "/boards/free/:postId/edit",
            name: "CommunityModifyView",
            component: CommunityModifyView
        },
        {
            path: "/boards/free/:postId",
            name: "CommunityView",
            component: CommunityView
        },
    ]
});

export default router;