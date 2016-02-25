//
// Created by raz on 1/7/16.
//

#include <jni.h>
#include <string.h>
#include <android/log.h>

// Implementation of native method mysum() of MainActivity class
void dfs(int n, int parent, int vertices, int *nodes, int **adj_mat);

extern "C"
/**
 * mysum(int vertices, int[][] adjMat)
 *
 * Takes a 2 dimensional array of ints with equal length and height, and the number of vertices in this array.
 * @returns 1 on cycle detection 0 on none
 */
JNIEXPORT jint JNICALL Java_dongs_lab1_MainActivity_mysum(JNIEnv *env, jobject thisObj,
                                                          jint vertices, jobjectArray adjMat) {
    jboolean isCopy;
    int ** adjArr;

    int adjLen = env->GetArrayLength(adjMat);
    adjArr = new int*[adjLen];

    for(int i = 0; i < adjLen; i++)
    {
        jintArray tempArr = (jintArray)env->GetObjectArrayElement(adjMat, i);
        adjArr[i] = new int[adjLen];
        adjArr[i] = env->GetIntArrayElements(tempArr, &isCopy);
        env->DeleteLocalRef(tempArr);
    }

    int nodes[vertices];
    memset(nodes, 0, sizeof(nodes));

    if (adjLen < 0 || adjLen % vertices != 0) {
        return -1;
    }

    dfs(0, -1, vertices, nodes, adjArr);

    if (isCopy == JNI_TRUE) {
        env->DeleteLocalRef(adjMat);
    }

    for (int i = 0; i < vertices; i++) {
        if (nodes[i] > 1) {
            return 1;
        }
    }

    return 0;
}
/**
 * void dfs(int n, int vertices, int *nodes, int **adj_mat)
 *
 * Takes n to specify the node to check, vertices as the number of vertices in the 2d array
 * *nodes as the nodes for visit checking and **adj_mat as the 2 dimensiona adjacency matrix.
 *
 * Recursive DFS function for a 2 dimensional adjacency array.
 *
 * @returns nothing
 */
void dfs(int n, int parent, int vertices, int *nodes, int **adj_mat) {
    nodes[n]++;

    for (int i = 0; i < vertices; i++) {
        if (adj_mat[i][n] && n != parent) {
            if (nodes[i]) {
                nodes[i]++;
                continue;
            }
            dfs(i, n, vertices, nodes, adj_mat);
        }
    }
}

