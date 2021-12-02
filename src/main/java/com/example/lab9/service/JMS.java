package com.example.lab9.service;

import com.example.lab9.model.*;

import javax.annotation.Resource;
import javax.ejb.Stateful;
import javax.jms.*;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

@Stateful
public class JMS {

    @Resource(name = "messageQueue")
    private Queue messageQueue;

    @Resource
    private ConnectionFactory connectionFactory;

    public Response sendUserJMS(User user) {
        System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES","*");
        try (final Connection connection = connectionFactory.createConnection();
             final Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
             final MessageProducer producer = session.createProducer(messageQueue)) {
            connection.start();
            final ObjectMessage jmsMessage = session.createObjectMessage(user);

            producer.send(jmsMessage);
            return Response.ok("User has been sent to queue").build();
        } catch (final Exception e) {
            throw new WebApplicationException(Response.status(500, "Error while sending user").build());
        }
    }

    public Response sendArticleJMS(Article article) {
        System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES","*");
        try (final Connection connection = connectionFactory.createConnection();
             final Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
             final MessageProducer producer = session.createProducer(messageQueue)) {
            connection.start();
            final ObjectMessage jmsMessage = session.createObjectMessage(article);

            producer.send(jmsMessage);
            return Response.ok("Article has been sent to queue").build();
        } catch (final Exception e) {throw new WebApplicationException(Response.status(500, "Error while sending article").build());
        }
    }

    public Response sendCommentJMS(Comment comment) {
        System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES","*");
        try (final Connection connection = connectionFactory.createConnection();
             final Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
             final MessageProducer producer = session.createProducer(messageQueue)) {
            connection.start();
            final ObjectMessage jmsMessage = session.createObjectMessage(comment);

            producer.send(jmsMessage);
            return Response.ok("Comment has been sent to queue").build();
        } catch (final Exception e) {
            throw new WebApplicationException(Response.status(500, "Error while sending comment").build());
        }
    }

    public User getUserJMS() {
        try (final Connection connection = connectionFactory.createConnection();
             final Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
             final MessageConsumer messageConsumer = session.createConsumer(messageQueue)) {

            connection.start();

            final Message jmsMessage = messageConsumer.receive(1000);

            if (jmsMessage == null) {
                System.out.println("jmsMessage is null");
            }

            ObjectMessage objectMessage = (ObjectMessage) jmsMessage;

            if (objectMessage == null) {
                System.out.println("Empty objectMessage");
            }
            return (User) objectMessage.getObject();
        } catch (final Exception e) {
            throw new RuntimeException("Caught exception from JMS when receiving a message", e);
        }
    }

    public Article getArticleJMS() {
        try (final Connection connection = connectionFactory.createConnection();
             final Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
             final MessageConsumer messageConsumer = session.createConsumer(messageQueue)) {

            connection.start();

            final Message jmsMessage = messageConsumer.receive(1000);

            if (jmsMessage == null) {
                System.out.println("jmsMessage is null");
            }

            ObjectMessage objectMessage = (ObjectMessage) jmsMessage;

            if (objectMessage == null) {
                System.out.println("Empty objectMessage");
            }
            return (Article) objectMessage.getObject();
        } catch (final Exception e) {
            throw new RuntimeException("Caught exception from JMS when receiving a message", e);
        }
    }

    public Comment getCommentJMS() {
        try (final Connection connection = connectionFactory.createConnection();
             final Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
             final MessageConsumer messageConsumer = session.createConsumer(messageQueue)) {

            connection.start();

            final Message jmsMessage = messageConsumer.receive(1000);

            if (jmsMessage == null) {
                System.out.println("jmsMessage is null");
            }

            ObjectMessage objectMessage = (ObjectMessage) jmsMessage;

            if (objectMessage == null) {
                System.out.println("Empty objectMessage");
            }
            return (Comment) objectMessage.getObject();
        } catch (final Exception e) {
            throw new RuntimeException("Caught exception from JMS when receiving a message", e);
        }
    }

}