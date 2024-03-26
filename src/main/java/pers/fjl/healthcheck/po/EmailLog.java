package pers.fjl.healthcheck.po;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_email_log")
@Builder
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EmailLog {

    @Id
    private String id;

    @Column(nullable = false)
    private String recipient;

    @Column(nullable = false)
    private String sender;

    @Column(nullable = false)
    private String subject;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createTime;

    @Column(nullable = false)
    private boolean isSent;

    private String errMsg;

}