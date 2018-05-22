package com.lucasurbas.counter.ui.detail.usecase;

import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MarkPostAsReadInteractorTest {

    private static final int POST_ID = 1;

//    private static final Post POST_1 = Post.builder().id(1).build();
//    private static final Post POST_1_READ = Post.builder().id(1).isRead(true).build();

//    @Mock
//    private PostRepository postRepository;
//
//    private InteractorSchedulers schedulers = new TestInteractorSchedulers();
//
//    private MarkPostAsReadInteractor sut;
//
//    @Before
//    public void setup() {
//        given(postRepository.updateSinglePost(any())).willReturn(Completable.complete());
//        sut = new MarkPostAsReadInteractor(postRepository, schedulers);
//    }
//
//    @Test
//    public void markPostAsRead() {
//        given(postRepository.getPostWithId(POST_ID)).willReturn(Single.just(POST_1));
//
//        TestObserver<Post> testObserver = sut.markPost(POST_ID).test();
//
//        testObserver.awaitTerminalEvent();
//        testObserver.assertNoErrors();
//        verify(postRepository).updateSinglePost(POST_1_READ);
//    }

}