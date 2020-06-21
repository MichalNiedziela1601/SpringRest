import * as React from "react";
import Card from "react-bootstrap/Card";
import Button from "react-bootstrap/Button";
import Accordion from "react-bootstrap/Accordion";
import Poll from "./Poll";

export default class PollCard extends React.Component{

    render() {
        let pollCardStyle = {
            color: '#000000'
        }
        return(

            <Accordion>
                <Card className="col-lg-12">
                    <Card.Body style={pollCardStyle}>
                        <Accordion.Toggle as={Button} variant="link" eventKey="0">{this.props.title}</Accordion.Toggle>
                        <Accordion.Collapse eventKey="0">
                            <Poll pollId={this.props.pollId} />
                        </Accordion.Collapse>
                    </Card.Body>
                </Card>
            </Accordion>
        )
    }

}