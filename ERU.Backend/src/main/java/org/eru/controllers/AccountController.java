package org.eru.controllers;

import org.eru.managers.MongoDBManager;
import org.eru.models.mongo.user.Post;
import jakarta.servlet.http.HttpServletRequest;
import org.eru.models.mongo.user.User;
import org.eru.models.mongo.user.actions.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class AccountController {

    @Autowired
    private HttpServletRequest request;

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/account/feed")
    public ResponseEntity<List<Post>> feed() {
        List<Post> posts = MongoDBManager.getInstance().getAllPosts();
        Collections.reverse(posts);
        return ResponseEntity.ok(posts);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/account/post")
    public ResponseEntity<Post> post(@RequestBody Post post) {
        User user = MongoDBManager.getInstance().getUserByAccountId(post.OwningUser);

        do {
            post.Identifier = UUID.randomUUID().toString();
        } while (MongoDBManager.getInstance().getPostByIdentifier(post.Identifier) != null);

        user.Posts.add(post);

        MongoDBManager.getInstance().updateUserByAccountId(user);

        return ResponseEntity.ok(post);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/post/{identifier}/comment")
    public ResponseEntity<Comment> comment(@RequestBody Comment comment, @PathVariable String identifier) {
        Post post = MongoDBManager.getInstance().getPostByIdentifier(identifier);
        if (post == null) {
            return ResponseEntity.ok(new Comment());
        }

        post.Comments.add(comment);

        MongoDBManager.getInstance().updatePostByIdentifier(post);

        return ResponseEntity.ok(comment);
    }
}
