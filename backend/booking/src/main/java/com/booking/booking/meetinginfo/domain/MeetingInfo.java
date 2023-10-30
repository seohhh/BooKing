package com.booking.booking.meetinginfo.domain;

import com.booking.booking.meeting.domain.Meeting;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "meetinginfos")
public class MeetingInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long meetinginfoId;

    @ManyToOne
    private Meeting meeting;

    private Date date;

    private String location;

    private Integer fee;
}