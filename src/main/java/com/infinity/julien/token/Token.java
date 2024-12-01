package com.infinity.julien.token;

import com.infinity.julien.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "tokens")
public class Token {
    @Id
    private String id;

    @Field("token")
    private String token;

    @Field("tokenType")
    private TokenType tokenType = TokenType.BEARER;

    private boolean revoked;

    private boolean expired;

    @DBRef
    private User user;

}
