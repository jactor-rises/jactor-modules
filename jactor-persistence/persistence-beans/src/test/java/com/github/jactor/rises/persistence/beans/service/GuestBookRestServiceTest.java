package com.github.jactor.rises.persistence.beans.service;

import com.github.jactorrises.client.dto.GuestBookDto;
import com.github.jactorrises.client.dto.GuestBookEntryDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("A GuestBookRestService")
class GuestBookRestServiceTest {

    private GuestBookRestService guestBookRestService;

    @Mock
    private RestTemplate restTemplateMock;

    @BeforeEach
    void initAndMock() {
        MockitoAnnotations.initMocks(this);
        guestBookRestService = new GuestBookRestService(restTemplateMock, "/guestBook");
    }

    @DisplayName("should use RestTemplate GET to fetch a guest book")
    @Test void shouldUseRestTemplateGetToFetchGuestBook() {
        when(restTemplateMock.getForEntity(anyString(), eq(GuestBookDto.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        guestBookRestService.fetch(1L);

        verify(restTemplateMock).getForEntity(eq("/guestBook/1"), eq(GuestBookDto.class));
    }

    @DisplayName("should use RestTemplate GET to fetch a guest book entry")
    @Test void shouldUseRestTemplateGetToFetchGuestBookEntry() {
        when(restTemplateMock.getForEntity(anyString(), eq(GuestBookEntryDto.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        guestBookRestService.fetchEntry(1L);

        verify(restTemplateMock).getForEntity(eq("/guestBook/entry/1"), eq(GuestBookEntryDto.class));
    }

    @DisplayName("should exchange guest book values with HttpMethod.POST when saving")
    @Test void shouldExchangeGuestBookValuesWithHttpPostWhenSaveOrUpdate() {
        when(restTemplateMock.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), eq(GuestBookDto.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        GuestBookDto guestBookDto = new GuestBookDto();
        guestBookRestService.saveOrUpdate(guestBookDto);

        verify(restTemplateMock).exchange(
                eq("/guestBook/persist"), eq(HttpMethod.POST), eq(new HttpEntity<>(guestBookDto)), eq(GuestBookDto.class)
        );
    }

    @DisplayName("should exchange guest book entry values with HttpMethod.POST when persisting")
    @Test void shouldExchangeGuestBookEntryValuesWithHttpPostWhenSaveOrUpdate() {
        when(restTemplateMock.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), eq(GuestBookEntryDto.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        GuestBookEntryDto guestBookEntryDto = new GuestBookEntryDto();
        guestBookRestService.saveOrUpdate(guestBookEntryDto);

        verify(restTemplateMock).exchange(
                eq("/guestBook/entry/persist"), eq(HttpMethod.POST), eq(new HttpEntity<>(guestBookEntryDto)), eq(GuestBookEntryDto.class)
        );
    }
}
