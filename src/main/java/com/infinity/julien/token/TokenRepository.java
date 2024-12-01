package com.infinity.julien.token;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends MongoRepository<Token, String> {  // Use String as the ID type

    @Query("{ 'user.id' : ?0, '$or' : [ { 'expired' : false }, { 'revoked' : false } ] }")
    List<Token> findAllValidTokenByUser(String userId);

    Optional<Token> findByToken(String token);
}
