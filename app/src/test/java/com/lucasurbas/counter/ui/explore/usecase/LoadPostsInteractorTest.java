package com.lucasurbas.counter.ui.explore.usecase;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import com.lucasurbas.counter.data.model.Post;
import com.lucasurbas.counter.data.repository.PostRepository;
import com.lucasurbas.counter.remote.RemotePostsService;
import com.lucasurbas.counter.remote.mapper.ApiPostMapper;
import com.lucasurbas.counter.remote.model.PostJson;
import com.lucasurbas.counter.remote.model.PostsJson;
import com.lucasurbas.counter.rx.InteractorSchedulers;
import rx.TestInteractorSchedulers;

@RunWith(MockitoJUnitRunner.class)
public class LoadPostsInteractorTest {

    private static final String USER_ID = "1";

    private static final PostJson POST_JSON_1 = PostJson.builder().id(1).build();
    private static final PostJson POST_JSON_2 = PostJson.builder().id(2).build();

    private static final Post POST_1 = Post.builder().id(1).build();
    private static final Post POST_2 = Post.builder().id(2).build();
    private static final Post POST_2_READ = Post.builder().id(2).isRead(true).build();


    @Mock
    private RemotePostsService remotePostsService;
    @Mock
    private PostRepository postRepository;
    @Mock
    private PostsJson postsJson;
    @Captor
    private ArgumentCaptor<List<Post>> argumentCaptor;

    private ApiPostMapper apiPostMapper = new ApiPostMapper();

    private InteractorSchedulers schedulers = new TestInteractorSchedulers();

    private LoadPostsInteractor sut;

    @Before
    public void setup() {
        given(postRepository.updatePosts(any())).willReturn(Completable.complete());
        sut = new LoadPostsInteractor(remotePostsService, postRepository, apiPostMapper, schedulers);
    }

    @Test
    public void loadPostsToRepositoryPersistReadFlag() {
        List<PostJson> postJsonList = new ArrayList<>();
        postJsonList.add(POST_JSON_1);
        postJsonList.add(POST_JSON_2);
        givenPostsFromRemote(postJsonList);
        List<Post> postListInRepo = new ArrayList<>();
        postListInRepo.add(POST_1);
        postListInRepo.add(POST_2_READ);
        givenPostsFromRepository(postListInRepo);

        TestObserver<Void> testObserver = sut.loadPostsToRepository(USER_ID).test();

        testObserver.awaitTerminalEvent();
        testObserver.assertNoErrors();
        verify(postRepository).updatePosts(argumentCaptor.capture());
        List<Post> postList = argumentCaptor.getValue();
        assertEquals(postJsonList.size(), postList.size());
        assertEquals(POST_1, postList.get(0));
        assertEquals(POST_2_READ, postList.get(1));
    }

    private void givenPostsFromRemote(List<PostJson> postJsonList){
        given(postsJson.getPostList()).willReturn(postJsonList);
        given(remotePostsService.getPosts(USER_ID)).willReturn(Single.just(postsJson));
    }

    private void givenPostsFromRepository(List<Post> postList){
        given(postRepository.getPosts()).willReturn(Single.just(postList));
    }

}