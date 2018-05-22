package com.lucasurbas.counter.data.repository;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.observers.TestObserver;
import com.lucasurbas.counter.data.model.Post;

@RunWith(MockitoJUnitRunner.class)
public class MemoryPostRepositoryTest {

    private static final Post POST_1 = Post.builder().id(1).build();
    private static final Post POST_1_READ = Post.builder().id(1).isRead(true).build();
    private static final Post POST_2 = Post.builder().id(2).build();

    private MemoryPostRepository sut;

    @Before
    public void setup() {
        sut = new MemoryPostRepository();
    }

    @Test
    public void savingPostsCompletes() {
        List<Post> postList = new ArrayList<>();
        postList.add(POST_1);

        TestObserver<Void> testSubscriber = sut.updatePosts(postList).test();

        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertNoErrors();
    }

    @Test
    public void savingPostsEmitsUpdate() {
        List<Post> postList = new ArrayList<>();
        postList.add(POST_1);
        TestObserver<List<Post>> testObserver = sut.getPostsUpdates().test();

        sut.updatePosts(postList).test();

        testObserver.assertNoErrors();
        assertEquals(1, testObserver.valueCount());
        List<Post> emittedPostList = testObserver.values().get(0);
        assertEquals(1, emittedPostList.size());
        assertEquals(POST_1, emittedPostList.get(0));
    }

//    @Test
//    public void savingPostsKeepsReadFlag() {
//        List<Post> postList = new ArrayList<>();
//        postList.add(POST_1);
//        postList.add(POST_2);
//        TestObserver<List<Post>> testSubscriber = sut.getPostsUpdates().test();
//        sut.updatePosts(Collections.singletonList(POST_1_READ)).test();
//
//        sut.updatePosts(postList).test();
//
//        testSubscriber.assertNoErrors();
//        assertEquals(2, testSubscriber.valueCount());
//        List<Post> emittedPostList = testSubscriber.values().get(1);
//        assertEquals(2, emittedPostList.size());
//        assertEquals(POST_1_READ, emittedPostList.get(0));
//        assertEquals(POST_2, emittedPostList.get(1));
//    }

    @Test
    public void savingEmptyListClearsRepository() {
        List<Post> postList = new ArrayList<>();
        postList.add(POST_1);
        postList.add(POST_2);
        TestObserver<List<Post>> testObserver = sut.getPostsUpdates().test();
        sut.updatePosts(postList).test();

        sut.updatePosts(new ArrayList<>()).test();

        testObserver.assertNoErrors();
        assertEquals(2, testObserver.valueCount());
        List<Post> emittedPostList = testObserver.values().get(1);
        assertEquals(0, emittedPostList.size());
    }

    @Test
    public void savingListClearsPostsNotIncludedInNewOne() {
        List<Post> postList = new ArrayList<>();
        postList.add(POST_1);
        postList.add(POST_2);
        TestObserver<List<Post>> testObserver = sut.getPostsUpdates().test();
        sut.updatePosts(postList).test();

        sut.updatePosts(Collections.singletonList(POST_1)).test();

        testObserver.assertNoErrors();
        assertEquals(2, testObserver.valueCount());
        List<Post> emittedPostList = testObserver.values().get(1);
        assertEquals(1, emittedPostList.size());
        assertEquals(POST_1, emittedPostList.get(0));
    }

    @Test
    public void updateSinglePostDoesntTouchOtherEntities() {
        List<Post> postList = new ArrayList<>();
        postList.add(POST_1);
        postList.add(POST_2);
        TestObserver<List<Post>> testObserver = sut.getPostsUpdates().test();
        sut.updatePosts(postList).test();

        sut.updateSinglePost(POST_1_READ).test();

        testObserver.assertNoErrors();
        assertEquals(2, testObserver.valueCount());
        List<Post> emittedPostList = testObserver.values().get(1);
        assertEquals(2, emittedPostList.size());
        assertEquals(POST_1_READ, emittedPostList.get(0));
        assertEquals(POST_2, emittedPostList.get(1));
    }

    @Test
    public void getPostsReturnsAllEntities() {
        List<Post> postList = new ArrayList<>();
        postList.add(POST_1);
        postList.add(POST_2);
        sut.updatePosts(postList).test();

        TestObserver<List<Post>> testObserver = sut.getPosts().test();

        testObserver.awaitTerminalEvent();
        testObserver.assertNoErrors();
        assertEquals(1, testObserver.valueCount());
        List<Post> emittedPostList = testObserver.values().get(0);
        assertEquals(2, emittedPostList.size());
        assertEquals(POST_1, emittedPostList.get(0));
        assertEquals(POST_2, emittedPostList.get(1));
    }

    @Test
    public void getPostWithIdReturnsCorrectPost() {
        List<Post> postList = new ArrayList<>();
        postList.add(POST_1);
        postList.add(POST_2);
        sut.updatePosts(postList).test();

        TestObserver<Post> testObserver = sut.getPostWithId(2).test();

        testObserver.awaitTerminalEvent();
        testObserver.assertNoErrors();
        assertEquals(1, testObserver.valueCount());
        Post emittedPost = testObserver.values().get(0);
        assertEquals(POST_2, emittedPost);
    }

}