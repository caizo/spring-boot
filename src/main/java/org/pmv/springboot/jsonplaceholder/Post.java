package org.pmv.springboot.jsonplaceholder;


public record Post(Integer userId,Long id, String title, String body) {
}
