import React from "react";
import { mount } from "enzyme";
import Profile from "./Profile";

describe("<Profile />", () => {
  it("matches snapshot", () => {
    const wrapper = mount(<Profile username="siluat" name="siluat" />);
    expect(wrapper).toMatchSnapshot();
  });
  it("renders username and name", () => {
    const wrapper = mount(<Profile username="siluat" name="siluat" />);

    expect(wrapper.props().username).toBe("siluat");
    expect(wrapper.props().name).toBe("siluat");

    const boldElement = wrapper.find("b");
    expect(boldElement.contains("siluat")).toBe(true);
    const spanElement = wrapper.find("span");
    expect(spanElement.text()).toBe("(siluat)");
  });
});
