package cn.edu.ncu.bbs.controller;

import cn.edu.ncu.bbs.domain.Comment;
import cn.edu.ncu.bbs.domain.CommentExample;
import cn.edu.ncu.bbs.domain.TopicExample;
import cn.edu.ncu.bbs.domain.security.MyToken;
import cn.edu.ncu.bbs.service.CommentService;
import cn.edu.ncu.bbs.service.Impl.CommentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping({""})
public class CommentController {

    @Autowired
    private CommentServiceImpl commentService;

    @PostMapping("/comment")
    public void CreateComment(@RequestBody Comment comment){
        MyToken user=(MyToken) SecurityContextHolder.getContext().getAuthentication();
        comment.setCommentFrom(user.getUserId());
        commentService.createComment(comment);



    }

    @GetMapping("/comment/{id}")
    public void getCommentByTopicId(Model model, @PathVariable int id){
       CommentExample commentExample =new CommentExample();
       List<Comment> comments = commentService.getCommentByTopicId(commentExample,id);
       model.addAttribute("comments",comments);

    }

    @GetMapping("/comment/delete/{id}")
    public void deleteCommentById(@PathVariable int id){
        commentService.deleteCommentById(id);
    }
}
