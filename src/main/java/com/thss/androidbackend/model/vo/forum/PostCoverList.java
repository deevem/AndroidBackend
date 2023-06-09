package com.thss.androidbackend.model.vo.forum;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

public record PostCoverList (
    List<PostCover> postList
) implements Serializable {
}
