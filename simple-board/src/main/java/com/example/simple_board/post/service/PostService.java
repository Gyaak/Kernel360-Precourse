package com.example.simple_board.post.service;

import com.example.simple_board.board.db.BoardRepository;
import com.example.simple_board.common.Api;
import com.example.simple_board.common.Pagination;
import com.example.simple_board.post.db.PostEntity;
import com.example.simple_board.post.db.PostRepository;
import com.example.simple_board.post.model.PostRequest;
import com.example.simple_board.post.model.PostViewRequest;
import com.example.simple_board.reply.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final ReplyService replyService;
    private final BoardRepository boardRepository;

    public PostEntity create(PostRequest postRequest) {

        var boardEntity = boardRepository.findById(postRequest.getBoardId()).get();

        var entity = PostEntity.builder()
                .boardEntity(boardEntity)
                .userName(postRequest.getUserName())
                .password(postRequest.getPassword())
                .email(postRequest.getEmail())
                .status("REGISTERED")
                .title(postRequest.getTitle())
                .content(postRequest.getContent())
                .postedAt(LocalDateTime.now())
                .build();
        return postRepository.save(entity);
    }

    public PostEntity view(PostViewRequest postViewRequest) {

        return postRepository.findFirstByIdAndStatusOrderByIdDesc(postViewRequest.getPostId(),"REGISTERED")
                .map( it -> {
                    if(!it.getPassword().equals(postViewRequest.getPassword())) {
                        var format = "비밀번호가 맞지 않습니다. %s vs %s";
                        throw new RuntimeException(String.format(format, it.getPassword(), postViewRequest.getPassword()));
                    }

                    var replyList = replyService.findAllByPostId(it.getId());
                    it.setReplyEntityList(replyList);
                    return it;
                }).orElseThrow(
                        () -> {
                            return new RuntimeException("존재하지 않는 게시글 : " + postViewRequest.getPostId());
                        }
                );
    }

    public Api<List<PostEntity>> all(Pageable pageable) {
        var list = postRepository.findAll(pageable);
        var pagination = Pagination.builder()
                .page(list.getNumber())
                .size(list.getSize())
                .currentElements(list.getNumberOfElements())
                .totalElements(list.getTotalElements())
                .totalPage(list.getTotalPages())
                .build();
        var response = Api.<List<PostEntity>>builder()
                .body(list.toList())
                .pagination(pagination)
                .build();
        return response;
    }

    public void delete(PostViewRequest postViewRequest) {
        postRepository.findFirstByIdAndStatusOrderByIdDesc(postViewRequest.getPostId(),"REGISTERED")
                .map( it -> {
                    if(!it.getPassword().equals(postViewRequest.getPassword())) {
                        var format = "비밀번호가 맞지 않습니다. %s vs %s";
                        throw new RuntimeException(String.format(format, it.getPassword(), postViewRequest.getPassword()));
                    }
                    it.setStatus("UNREGISTERED");
                    postRepository.save(it);
                    return it;
                }).orElseThrow(
                        () -> {
                            return new RuntimeException("존재하지 않는 게시글 : " + postViewRequest.getPostId());
                        }
                );
    }
}
