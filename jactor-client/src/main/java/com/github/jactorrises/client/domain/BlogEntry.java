package nu.hjemme.client.domain;

public interface BlogEntry extends Persistent<Long>, Entry {

    Blog getBlog();
}
